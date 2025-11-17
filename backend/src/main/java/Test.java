public class Test {

    public void test(){
        Socket socket = new Socket();
        socket.subscribe("labeuteu", this::onEvent);

    }
    private void onEvent(Object object){

    }
}
