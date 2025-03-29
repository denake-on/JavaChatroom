package com.ly.chatroom.client.version;

import java.util.Scanner;

public class InputUtility {

    private static Scanner scanner = new Scanner(System.in);
//获取用户输入的字符串并返回
    public static String getString() {
        return scanner.nextLine();
    }
//获取用户输入的整数并返回
    public static String getIntAsString() {
        int number = scanner.nextInt();
        scanner.nextLine();
        return String.valueOf(number);
    }
//   关闭scanner
    public static void close() {
        scanner.close();
    }
}
