package brook;

import java.awt.event.KeyEvent;

public class SceneEdit extends Scene {

    private boolean isSceneChanging = false;
    private float sceneChangeTime = 1.0f;

    public SceneEdit() {
        System.out.println("Scene editor has started");
    }

    @Override
    public void update(float dt) {

        if (!isSceneChanging && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            isSceneChanging = true;
        }

        if (isSceneChanging && sceneChangeTime > 0) {
            sceneChangeTime -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (isSceneChanging) {
            Window.changeScene(1);
        }
    }
}
