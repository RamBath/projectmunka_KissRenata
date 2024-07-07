package test;

public class ResourceTest {
    public static void main(String[] args) {
        String[] tileImages = {
            "tile01.png", "tile02.png", "tile03.png", "tile04.png", "tile05.png",
            "tile06.png", "tile07.png", "tile08.png", "tile09.png", "tile10.png",
            "tile11.png", "tile12.png", "tile13.png", "tile14.png", "tile15.png",
            "tile16.png", "tile17.png"
        };
        
        for (String image : tileImages) {
            java.net.URL imageUrl = ResourceTest.class.getResource("/images/" + image);
            if (imageUrl != null) {
                System.out.println(image + " loaded successfully.");
            } else {
                System.out.println("Failed to load " + image);
            }
        }
    }
}
