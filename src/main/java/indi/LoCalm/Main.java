package indi.LoCalm;

import cn.hutool.core.lang.Console;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        run(new HashMap<>());

    }
    private static void run(@NotNull Map<String, String> setGetField){
        for (Map.Entry<String, String> entry : setGetField.entrySet()) {
            Console.log(entry);
        }
        System.out.println(1111);


    }


}
