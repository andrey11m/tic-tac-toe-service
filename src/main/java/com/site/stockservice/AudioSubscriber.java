package com.site.stockservice;

import lombok.SneakyThrows;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;

public class AudioSubscriber implements Consumer<byte[]> {

    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    private int index = 0;

    @SneakyThrows
    public AudioSubscriber() {
        audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

//        DataLine.Info dataLineInfoT = new DataLine.Info(TargetDataLine.class, audioFormat);
//        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfoT);
//        targetDataLine.open(audioFormat);
//        targetDataLine.start();
    }

    @SneakyThrows
    @Override
    public void accept(byte[] tempBuffer) {
        System.out.println(Arrays.toString(tempBuffer));
//        byteArrayOutputStream = new ByteArrayOutputStream();
//        while(index != 3){
//            int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
//            if(cnt > 0){
//                //Сохраняем данные в выходной поток
//                byteArrayOutputStream.write(tempBuffer, 0, cnt);
//            }
//            byteArrayOutputStream.write(tempBuffer, 0, tempBuffer.length);
//            index++;
//        }
//        byteArrayOutputStream.close();
//        playAudio();
//        index = 0;
    }

    @SneakyThrows
    public void playAudio() {
        byte[] audioData = byteArrayOutputStream.toByteArray();

        InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length/audioFormat.getFrameSize());
        //Создаем поток для проигрывания
        // данных и запускаем его
        // он будет работать пока
        // все записанные данные не проиграются
        int cnt;
        // цикл пока не вернется -1
        byte[] tempBuffer = new byte[10000];
        while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
            if(cnt > 0){
                //Пишем данные во внутренний
                // буфер канала
                // откуда оно передастся
                // на звуковой выход
                sourceDataLine.write(tempBuffer, 0, cnt);
            }
        }
        sourceDataLine.drain();
    }
    class PlayThread extends Thread{
        byte[] tempBuffer = new byte[10000];

        public void run(){
            try{
                int cnt;
                // цикл пока не вернется -1

                while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
                    if(cnt > 0){
                        //Пишем данные во внутренний
                        // буфер канала
                        // откуда оно передастся
                        // на звуковой выход
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }

                sourceDataLine.drain();
                sourceDataLine.close();
            }catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }
    private AudioFormat getAudioFormat(){
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
