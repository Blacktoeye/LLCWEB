package llcweb.com.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import llcweb.com.controller.admin.ImageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by tengj on 2017/3/13.
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    private final static Logger logger=LoggerFactory.getLogger(ImageController.class);
    //获取配置文件中图片的路径
    @Value("${image.location}")
    private String imgPath;
    @Value("${file.location}")
    private String filePath;
    //访问图片方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(imgPath.equals("") || imgPath.equals("${image.location}")){
            String imagesPath = MyWebMvcConfigurerAdapter.class.getClassLoader().getResource("").getPath();
            if(imagesPath.indexOf(".jar")>0){
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            }else if(imagesPath.indexOf("classes")>0){
                imagesPath = "file:"+imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/"))+"/images/";
            imgPath = imagesPath;
        }
        logger.info("imagesPath="+imgPath);
        //linux 下
        //registry.addResourceHandler("/images/**").addResourceLocations("file:"+"/root/LLCWEB/images/");
        //registry.addResourceHandler("/files/**").addResourceLocations("file:"+"/root/LLCWEB/files/");
        //windows下  C:\Users\Ricardo\IdeaProjects\LLCWEB\files\   E:\LLCWEB\files\
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+"E:\\LLCWEB\\images\\");
        registry.addResourceHandler("/files/**").addResourceLocations("file:"+"E:\\LLCWEB\\files\\");
        //registry.addResourceHandler("/images/**").addResourceLocations("file:"+"C:\\Users\\Ricardo\\IdeaProjects\\LLCWEB\\images\\");
        //registry.addResourceHandler("/files/**").addResourceLocations("file:"+"C:\\Users\\Ricardo\\IdeaProjects\\LLCWEB\\files\\");

        super.addResourceHandlers(registry);
    }

    /**
     * 配置静态访问资源
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //自定义项目内目录
//        //registry.addResourceHandler("/my/**").addResourceLocations("classpath:/my/");
//        //指向外部目录
//        registry.addResourceHandler("/my/**").addResourceLocations("file:E:/my/");
//        super.addResourceHandlers(registry);
//    }

    /**
     * 以前要访问一个页面需要先创建个Controller控制类，在写方法跳转到页面
     * 在这里配置后就不需要那么麻烦了，直接访问http://localhost:8080/toLogin就跳转到login.html页面了
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("admin/login");
        super.addViewControllers(registry);
    }

    /**
     * 拦截器
     * @param registry
     */
    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/toLogin","/login","/admin/test.html");
        super.addInterceptors(registry);
    }
    */

    /**
     * 配置fastJson
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
        super.configureMessageConverters(converters);
    }
}

