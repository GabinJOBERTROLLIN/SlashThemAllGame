package com.project.RunGame.game.controller.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.project.RunGame.dto.MessageDto;
import com.project.RunGame.game.controller.websockets.eventPublishers.InputEventPublisher;
import com.project.RunGame.game.controller.websockets.eventPublishers.MapEventPublisher;
import com.project.RunGame.game.controller.websockets.eventPublishers.MovementEventPublisher;
import com.project.RunGame.helper.DirectionEnum;
import com.project.RunGame.map.model.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class GameWebSocket extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    InputEventPublisher inputEventPublisher;
    MapEventPublisher mapEventPublisher;
    MovementEventPublisher movementEventPublisher;

    @Autowired
    public GameWebSocket(InputEventPublisher inputEventPublisher, MapEventPublisher mapEventPublisher, MovementEventPublisher movementEventPublisher) {
        this.inputEventPublisher = inputEventPublisher;
        this.mapEventPublisher = mapEventPublisher;
        this.movementEventPublisher = movementEventPublisher;
    }


    private void publishMapEvent(JsonNode dataJson, String roomId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tileJson = dataJson.get("tiles");


        CollectionType stringSetType = objectMapper.getTypeFactory()
                .constructCollectionType(Set.class, String.class);
        Set<String> coordStrings;
        try {
            coordStrings = objectMapper.readValue(tileJson.traverse(), stringSetType);
            Set<Coordinates> coordinates = coordStrings.stream()
                    .map(str -> {
                        String[] parts = str.split(";");
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Invalid coordinate format: " + str);
                        }
                        int coordX = Integer.parseInt(parts[0]);
                        int coordY = Integer.parseInt(parts[1]);
                        return new Coordinates(coordX, coordY);
                    })
                    .collect(Collectors.toSet());
            this.mapEventPublisher.publishMapEvent(coordinates, roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publishSkillEvent(JsonNode dataJson, String roomId) {
        String skillName = dataJson.get("skill").asText();
        String skillDirectionStr = dataJson.get("direction").asText();
        DirectionEnum skillDirection = DirectionEnum.valueOf(skillDirectionStr);
        this.inputEventPublisher.publishInputEvent(skillName, skillDirection, roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonMessage = objectMapper.readTree(message.getPayload());
            String type = jsonMessage.get("type").asText();
            JsonNode dataJson = jsonMessage.get("data");
            String roomId = jsonMessage.get("roomId").asText();


            if ("map".equals(type) && dataJson != null) {
                this.publishMapEvent(dataJson, roomId);
            } else if ("skill".equals(type) && dataJson != null) {

                this.publishSkillEvent(dataJson, roomId);
            } else if ("position".equals(type) && dataJson != null) {
                this.publishPositionEvent(dataJson, roomId);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void publishPositionEvent(JsonNode dataJson, String roomId) {
        double x = dataJson.get("x").asDouble();
        double y = dataJson.get("y").asDouble();
        String skillDirectionStr = dataJson.get("direction").asText();
        DirectionEnum skillDirection = DirectionEnum.valueOf(skillDirectionStr);
        com.project.RunGame.helper.Coordinates coord = new com.project.RunGame.helper.Coordinates(x, y);
        this.movementEventPublisher.publishMovementEvent(coord, roomId, skillDirection);
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getUri().getQuery().split("=")[1];
        userSessions.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        System.out.println("webSocket closed");

    }

    //calling this function from other functions
    public void sendMessageToRoom(String roomId,String type, Object object) {
    	RestTemplate restTemplate = new RestTemplate();
		 String backendUrl = System.getenv("BACKEND_URL");
		 String url = backendUrl + "/users/users?roomId={roomId}";

        Map<String, String> params = new HashMap<>();
        params.put("roomId", roomId);

        Set<String> userIds = restTemplate.getForObject(url, Set.class, params);

        for (String userId : userIds) {
            this.sendMessageToUser(userId, type, object);
        }

    }

    public void sendMessageToUser(String userId, String type, Object object) {
        WebSocketSession session = this.userSessions.get(userId);
        String json = "";
        MessageDto messageDto = new MessageDto(type, object);
        ObjectWriter ow = new ObjectMapper().writer();
        try {
            json = ow.writeValueAsString(messageDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            synchronized (session) {
                session.sendMessage(new TextMessage(json));
            }

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("error");
        }
    }
}

