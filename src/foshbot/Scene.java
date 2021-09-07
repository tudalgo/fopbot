package foshbot;

public interface Scene {

    int getWidth();

    int getHeight();

    void init(World world);

    void run(World world);
}
