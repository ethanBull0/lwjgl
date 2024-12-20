package brook;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
public class Window {

    private int width;
    private int height;
    private String title;
    private long glfwWindow; //memory address
    private float r, g, b, a;

    private static Window window = null;

    private static Scene currentScene;

    // The window handle
    private Window() {
        this.width = 640;
        this.height = 480;
        this.title = "Brook engine";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch(newScene) {
            case 0:
                currentScene = new SceneEdit();
                break;
            default:
                assert false: "Cannot find scene" + newScene;
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window; //engine contains one window at a time
    }



    public void init() {
        GLFWErrorCallback.createPrint(System.err).set(); //System.err.println

        //init glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Cannot initialize GLFW");
        }

        //glfw config
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //window create
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        //width, height, title, monitor, sharing
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Could not create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //enable vsynce
        glfwSwapInterval(1);

        //make window visible
        glfwShowWindow(glfwWindow);

        //important!
        GL.createCapabilities();
    }

    public void loop() {

        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a); //red
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void run() {
        System.out.println("LWJGL running on version" + Version.getVersion());

        init();
        loop();
        //when loop ends
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}