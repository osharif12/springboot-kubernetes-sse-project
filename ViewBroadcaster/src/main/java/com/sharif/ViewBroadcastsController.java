package com.sharif;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/server-events")
public class ViewBroadcastsController {
    /**
     * ExecutorService is a JDK API that simplifies running tasks in asynchronous mode. Generally speaking,
     * ExecutorService automatically provides a pool of threads and an API for assigning tasks to it.
     */
    private final ExecutorService cachedThreadPool;

    /**
     * The newCachedThreadPool() method of Executors class creates a thread pool that creates new threads as needed
     * but will reuse previously constructed threads when they are available.
     */
    ViewBroadcastsController(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    /**
     * Server-Sent-Events (SSE) is an HTTP standard that allows a web application to handle a unidirectional event
     * stream and receive updates whenever server emits data. Provides a standardized way to send text data to the
     * client which can be easily consumed by a web browser or any SSE-compatible client
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/view-broadcasts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter viewBroadcasts() throws Exception{
        long maxAsyncTimeout = Long.MAX_VALUE;
        SseEmitter emitter = new SseEmitter(maxAsyncTimeout);

        // method that executes a runnable command using thread pool
        this.cachedThreadPool.execute(() -> {
            new ClientServer(50, emitter).run();
        });

        return emitter;
    }

    private class ClientServer implements Runnable {
        private int count;
        private SseEmitter emitter;

        public ClientServer(int count, SseEmitter emitter){
            this.count = count;
            this.emitter = emitter;
        }

        @Override
        public void run(){
            receiveHelloWorldMessages(this.count, this.emitter);
        }

        /**
         *
         * @param count
         * @param emitter
         */
        public void receiveHelloWorldMessages(int count, SseEmitter emitter){
            try {
                while(count-- > 0){
                    // connects to broadcaster which is the name of the kubernetes service that redirects request to broadcaster pod
                    Socket socket = new Socket("broadcaster", 8081);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String msg = bufferedReader.readLine();
                    System.out.println("Broadcast message received: " + msg);
                    emitter.send(msg);

                    socket.close();
                }

                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }
}
