package com.example.cloudconfigserver.WatchFile;

import okhttp3.*;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件变化监听器
 * 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
 * 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
 * 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
 */
@Component
public class FileListener extends FileAlterationListenerAdaptor {

    private final OkHttpClient okHttpClient = new OkHttpClient();

    private Logger log = LoggerFactory.getLogger(FileListener.class);

    @Autowired
    private DiscoveryClient discoveryClient;
    /**
     * 文件创建执行
     */
    @Override
    public void onFileCreate(File file) {
        log.info("[新建]:" + file.getAbsolutePath());
    }

    /**
     * 文件创建修改
     */
    @Override
    public void onFileChange(File file) {
        String serviceId = "cloud-config-server";
        //通过eureka获取服务列表
        //List<String> services = discoveryClient.getServices();
        List<String> services = new ArrayList<>();
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        for(ServiceInstance serviceInstance : serviceInstances){
            services.add(String.format("%s",serviceInstance.getUri()));
        }
        services.forEach(System.out::println);

        for (String url : services){
            String postBody = "{\"type\":\"post json提交\"}";
            Request request = new Request.Builder()
                    .url( url + "/actuator/bus-refresh")
                    .addHeader("content-type", "application/json;charset:utf-8")
                    .post(RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            postBody))
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.body().string());
                if(response.isSuccessful()){
                    break;
                }else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                log.info("修改配置文件出错");
                e.printStackTrace();
            }
        }

        log.info("[修改]:" + file.getAbsolutePath());
    }

    /**
     * 文件删除
     */
    @Override
    public void onFileDelete(File file) {
        log.info("[删除]:" + file.getAbsolutePath());
    }

    /**
     * 目录创建
     */
    @Override
    public void onDirectoryCreate(File directory) {
        log.info("[新建]:" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    @Override
    public void onDirectoryChange(File directory) {
        log.info("[修改]:" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    @Override
    public void onDirectoryDelete(File directory) {
        log.info("[删除]:" + directory.getAbsolutePath());
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStop(observer);
    }

    public static void main(String[] args) throws Exception {
        // 监控目录
        String rootDir = "E:\\temp";
        // String rootDir = "classpath:/config";
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(1);
        // 创建过滤器
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".properties"));
        IOFileFilter filter = FileFilterUtils.or(directories, files);
        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
        //不使用过滤器
        //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
        observer.addListener(new FileListener());
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        monitor.start();
    }
}
