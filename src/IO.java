// import java.io.File;
// import java.util.Scanner;

// public class IO {
//     private static int countNode;

//     public static void printCompressionStats(File originalFile, File compressedFile, QuadtreeNode root) {
//         try {
//             long originalSizeBytes = originalFile.length();
//             long compressedSizeBytes = compressedFile.length();
            
//             double originalSizeKB = originalSizeBytes / 1024.0;
//             double compressedSizeKB = compressedSizeBytes / 1024.0;
            
//             double compressionRatio = 1 - (compressedSizeKB / originalSizeKB);
//             double compressionPercentage = compressionRatio * 100;
            
//             countNode = 0;
//             int treeDepth = getTreeDepth(root);
            
//             System.out.printf("Image size before compressed: %.2f KB\n", originalSizeKB);
//             System.out.printf("Image size after compressed: %.2f KB\n", compressedSizeKB);
//             System.out.printf("Compression percentage: %.2f%%\n", compressionPercentage);
//             System.out.printf("Tree depth: %d\n", treeDepth);
//             System.out.printf("Total node(s): %d\n\n", countNode);            
//         } catch (Exception e) {
//             System.err.println("Error reading file or calculating compression: " + e.getMessage());
//         }
//     }
    
//     // Recursive helper method to get the depth of the Quadtree
//     public static int getTreeDepth(QuadtreeNode node) {
//         if (node == null || node.children == null || node.children.length == 0) {
//             return 1;
//         }
        
//         int maxDepth = 0;
//         for (QuadtreeNode child : node.children) {
//             maxDepth = Math.max(maxDepth, getTreeDepth(child));
//         }
//         countNode += 4;
//         return maxDepth + 1;
//     }

//     public static void printHeader() {
//         System.out.println("\n╔══════════════════════════════════════════════════╗");
//         System.out.println("║         IMAGE COMPRESSION WITH QUADTREE          ║");
//         System.out.println("║               (CLI Version)                      ║");
//         System.out.println("╚══════════════════════════════════════════════════╝\n");
//     }

//     public static void printFooter() {
//         System.out.println("\n╔══════════════════════════════════════════════════╗");
//         System.out.println("║          THANKYOU FOR USING THIS PROGRAM         ║");
//         System.out.println("║           IMAGE COMPRESSION WITH QUADTREE        ║");
//         System.out.println("╚══════════════════════════════════════════════════╝");
//     }

//     public static int getValidatedIntInput(Scanner scanner, String prompt, int min, int max) {
//         int value;
//         while (true) {
//             try {
//                 System.out.print(prompt);
//                 value = Integer.parseInt(scanner.nextLine().trim());
//                 if (value < min || value > max) {
//                     System.out.println("Your input is not valid, please enter the correct input\n");
//                 } else {
//                     return value;
//                 }
//             } catch (NumberFormatException e) {
//                 System.out.println("Input must be integer!\n");
//             }
//         }
//     }

//     public static double getValidatedDoubleInput(Scanner scanner, String prompt) {
//         while (true) {
//             try {
//                 System.out.print(prompt);
//                 return Double.parseDouble(scanner.nextLine().trim());
//             } catch (NumberFormatException e) {
//                 System.out.println("Input must be decimal!\n");
//             }
//         }
//     }

//     public static File getValidatedOutputFile(Scanner scanner, String prompt) {
//         while (true) {
//             System.out.print(prompt);
//             String path = scanner.nextLine().trim();
//             File file = new File(path);

//             if (file.exists()) {
//                 System.out.println("⚠️ File already exist: " + file.getName());
//                 System.out.print("Do you want to overwrite this file? (y/n): ");
//                 String response = scanner.nextLine().trim().toLowerCase();
//                 if (response.equals("y")) {
//                     return file;
//                 } else {
//                     System.out.println("Silakan masukkan path file output lain.\n");
//                     continue;
//                 }
//             } else {
//                 return file;
//             }
//         }
//     }

//     public static void showErrorMethodMenu() {
//         System.out.println("\nPilih metode perhitungan error:");
//         System.out.println("[1] Variance");
//         System.out.println("[2] Mean Absolute Deviation");
//         System.out.println("[3] Max Pixel Difference");
//         System.out.println("[4] Entropy");
//     }
// }