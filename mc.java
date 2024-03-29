// This is a very basic game that lets you move around and place blocks in a 3D world
// You need to have Java and LWJGL (Lightweight Java Game Library) installed to run this code
// This code is generated by Bing and is not guaranteed to work or be bug-free

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class SimpleMinecraft {

    // The window handle
    private long window;

    // The size of the window
    private int width = 800;
    private int height = 600;

    // The camera position and rotation
    private float x = 0f;
    private float y = 0f;
    private float z = 0f;
    private float rx = 0f;
    private float ry = 0f;

    // The speed of the camera movement
    private float speed = 0.1f;

    // The mouse sensitivity
    private float sensitivity = 0.1f;

    // The block size
    private float blockSize = 1f;

    // The world size
    private int worldSize = 16;

    // The world array that stores the block types
    private int[][][] world;

    // The random number generator
    private Random random;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Simple Minecraft", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop

            if ( key == GLFW_KEY_W && action == GLFW_PRESS )
                move(0f, 0f, -speed); // Move forward

            if ( key == GLFW_KEY_S && action == GLFW_PRESS )
                move(0f, 0f, speed); // Move backward

            if ( key == GLFW_KEY_A && action == GLFW_PRESS )
                move(-speed, 0f, 0f); // Move left

            if ( key == GLFW_KEY_D && action == GLFW_PRESS )
                move(speed, 0f, 0f); // Move right

            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS )
                move(0f, speed, 0f); // Move up

            if ( key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS )
                move(0f, -speed, 0f); // Move down

            if ( key == GLFW_KEY_LEFT_CONTROL && action == GLFW_PRESS )
                speed *= 2f; // Increase speed

            if ( key == GLFW_KEY_LEFT_CONTROL && action == GLFW_RELEASE )
                speed /= 2f; // Decrease speed

            if ( key == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS
