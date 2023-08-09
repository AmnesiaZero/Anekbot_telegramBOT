package utils;

import lombok.extern.log4j.Log4j;

import java.util.Queue;
@Log4j
public class PrintUtils {
    public static void printQue(Queue queue){
        for (Object object:queue) {
            log.info(object);
        }
    }
}
