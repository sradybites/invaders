package engine;

import invaders.Game;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS= 75;

    public static final int TARGET_UPS= 30;

    private final Window window;

    private final Timer timer;

    private final Game game;

    public GameEngine(String windowTitle, int width, int height, boolean vSync,
        Game game) throws Exception {
        window= new Window(windowTitle, width, height, vSync);
        this.game= game;
        timer= new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        game.init();
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator= 0f;
        float interval= 1f / TARGET_UPS;

        boolean running= true;
        while (running && !window.windowShouldClose()) {
            elapsedTime= timer.getElapsedTime();
            accumulator+= elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator-= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    protected void cleanup() {
        game.cleanup();
    }

    private void sync() {
        float loopSlot= 1f / TARGET_FPS;
        double endTime= timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {}
        }
    }

    protected void input() {
        game.input(window);
    }

    protected void update(float interval) {
        game.update(interval);
    }

    protected void render() {
        game.render(window);
        window.update();
    }
}