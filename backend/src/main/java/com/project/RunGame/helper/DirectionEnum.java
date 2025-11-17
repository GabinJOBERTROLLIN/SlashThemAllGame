package com.project.RunGame.helper;


public enum DirectionEnum {
    Up, Down, Left, Right, UpLeft, UpRight, DownRight, DownLeft;

    public static DirectionEnum getOpposite(DirectionEnum direction) {
        if (direction.equals(Up)) {
            return Down;
        } else if (direction.equals(Down)) {
            return Up;
        } else if (direction.equals(Left)) {
            return Right;
        } else if (direction.equals(Right)) {
            return Left;
        } else if (direction.equals(UpLeft)) {
            return DownRight;
        } else if (direction.equals(UpRight)) {
            return DownLeft;
        } else if (direction.equals(DownLeft)) {
            return UpRight;
        } else if (direction.equals(DownRight)) {
            return UpLeft;
        } else {
            return null;
        }
    }
}
