import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: Reactor
 * @description: Reactor 学习类
 * @author: guangbai
 * @create: 2018-07-01 20:24
 **/
public class Reactor implements Runnable {

    final Selector selector;

    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException{
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        SelectionKey sk = serverSocket.register(selector,SelectionKey.OP_ACCEPT);
//        sk.attach(new Acceptor());

    }


    void dispatch(SelectionKey k){
        Runnable r = (Runnable) (k.attachment());
        if(r!=null){
            r.run();
        }
    }

    @Override
    public void run() {

        try{
            while(!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while(it.hasNext())
                    dispatch((SelectionKey) it.next());
                selected.clear();
            }

        }catch (IOException e){

        }

    }

    class Acceptor implements Runnable {


        @Override
        public void run() {
            try{
                SocketChannel c = serverSocket.accept();
                if(c!=null)
                    new Hand
            }catch (IOException ex){

            }


        }
    }

    final class Handler implements Runnable {


        static  ThreadPoolExecutor pool = new ThreadPoolExecutor();
        static  final int PROCESSING = 3;
        final SocketChannel socket;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static  final int READING = 0,SENDING = 1;
        int state = READING;

        Handler(Selector sel, SocketChannel c) throws IOException{
            socket = c;
            sk = socket.register(sel,0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete(){};
        boolean outputIsComplete(){};
        void process(){};


        @Override
        public void run() {
            if (state == READING){};

        }

       synchronized void  read() throws IOException {
            socket.read(input);
            if(inputIsComplete()){
                process();
                state = PROCESSING;
                pool.execute(new Processer()ß);
            }
        }

        void send() throws IOException {
            socket.write(output);
            if(outputIsComplete()) sk.cancel();
        }

        synchronized void processAndHandOff(){
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }

        class Processer implements  Runnable {
            @Override
            public void run() {
                processAndHandOff();
            }
        }
    }
}
