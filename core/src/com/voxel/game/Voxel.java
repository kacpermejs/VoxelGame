package com.voxel.game;

public class Voxel {
    public static class Vertices {

        private static float[] texCoords = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
        };

        //face side encoding
        float dataSouth = (float) (0b00000000 & 0xFF);
        float dataEast  = (float) (0b00000001 & 0xFF);
        float dataNorth = (float) (0b00000010 & 0xFF);
        float dataWest  = (float) (0b00000011 & 0xFF);
        float dataTop  = (float) (0b00000100 & 0xFF);
        float dataBottom  = (float) (0b00000101 & 0xFF);

        public static float[] south = {
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
        };

        public static float[] east = {
             0.5f,  0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
        };

        //south to north
        public static float[] all = {
            0f,  0f,  0f, // ------
            0f, -1f,  0f, //  south
            1f, -1f,  0f, //  wall (counterclockwise)
            1f,  0f,  0f, // ______

            1f,  0f,  -1f, // ------
            1f, -1f,  -1f, //  north
            0f, -1f,  -1f, //  wall (counterclockwise)
            0f,  0f,  -1f, // ______
        };
    }
}
