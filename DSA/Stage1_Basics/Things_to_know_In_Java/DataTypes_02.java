package Stage1_Basics.Things_to_know_In_Java;

public class DataTypes_02 {

    // Method to return size of data types in bytes
    static int dataTypeSize(String str) {

        // If the input is "Character", return 2 bytes
        if (str.equals("Character")) {
            return 2;
        }

        // If the input is "Integer", return 4 bytes
        else if (str.equals("Integer")) {
            return 4;
        }

        // If the input is "Long", return 8 bytes
        else if (str.equals("Long")) {
            return 8;
        }

        // If the input is "Float", return 4 bytes
        else if (str.equals("Float")) {
            return 4;
        }

        // If the input is "Double", return 8 bytes
        else if (str.equals("Double")) {
            return 8;
        }

        // Return -1 if data type is invalid
        return -1;
    }

    public static void main(String[] args) {

        System.out.println(dataTypeSize("Character"));
        System.out.println(dataTypeSize("Integer"));
        System.out.println(dataTypeSize("Long"));
        System.out.println(dataTypeSize("Float"));
        System.out.println(dataTypeSize("Double"));
    }
}