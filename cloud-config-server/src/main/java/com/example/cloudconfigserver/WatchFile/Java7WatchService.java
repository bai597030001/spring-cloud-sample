package com.example.cloudconfigserver.WatchFile;

import java.io.IOException;
import java.nio.file.*;

/**
 * 监听文件变化
 *
 * 要实现监听文件的变更然后马上做出响应，在Java7之前，除了不断的循环似乎没什么好办法，
 * 而且实现也很麻烦。Java7推出了一个WatchService来解决这个问题。
 *
 * WatchKey:
 *      此类代表着一个Object,(文件或者文件夹)注册到WatchService后，返回一个WatchKey类。
 *      反应当前所注册的Object状态的类。此类封装了不同事件的状态值，比如，当文件(文件夹)被修改或者被删除或者创建的时候，此类首先产生一个信号量,等待消费者来取并且该WatchKey将会进入到WatchService的队列中。如果WatchKey已经进入到队列当中，但是又有了变化，将不会再次进入到队列当中。
 *
 *      Java7WatchService.take()方法可以取到队列的WatchKey.
 *
 * WatchEvent:
 *      此类代表文件的一个具体的文件变更事件
 *
 * 原理:
 *      把一个Path注册到WatchService,并告诉它你感兴趣的变化事件，如果WatchService检测到变更，
 *      会以事件的形式发送到一个队列，你只需不断地从WatchService中等待事件的到来并确认是自己
 *      关注的事件，然后做相应的处理，最后需要reset表示已经处理了这个事件。
 *
 * 存在的问题:
 *      （1）当文件修改时，会被调用两次，即输出两个相同的修改。
 *      （2）不能对其子文件夹进行监控，只能提示目录被修改。
 *      （3）无法对文件类型进行过滤。
 */
public class Java7WatchService {

    public static void main(String[] a) {

        final Path path = Paths.get("E:/temp/cloud-config-client-dev.properties");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            //给path路径加上文件观察服务
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE);
            // start an infinite loop
            while (true) {
                final WatchKey key = watchService.take();

                for (WatchEvent<?> watchEvent : key.pollEvents()) {

                    final WatchEvent.Kind<?> kind = watchEvent.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    //创建事件
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("create file event");
                    }
                    //修改事件
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("modify file event");
                    }
                    //删除事件
                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("delete file event");
                    }
                    // get the filename for the event
                    final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                    final Path filename = watchEventPath.context();
                    // print it out
                    System.out.println(kind + " -> " + filename);

                }
                // reset the keyf
                boolean valid = key.reset();
                // exit loop if the key is not valid (if the directory was
                // deleted,for
                if (!valid) {
                    break;
                }
            }

        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
        }
    }
}
