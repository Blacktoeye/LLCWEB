package llcweb.com.controller.admin;

import llcweb.com.dao.repository.ImageRepository;
import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.entity.UsefulImage;
import llcweb.com.domain.models.Image;
import llcweb.com.domain.models.Users;
import llcweb.com.exception.BusinessException;
import llcweb.com.service.ImageService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.ImageUtil;
import llcweb.com.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author haien
 * @Description 图片控制器
 * @Date 22:05 2018/9/5
 **/
@RestController
@RequestMapping("/image")
public class ImageController {
    private final static Logger logger=LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * @Author haien
     * @Description 更新/添加图片
     * @Date 2018/9/5
     * @Param [image]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @PostMapping("/save")
    @Transactional
    public Map<String,Object> saveImage(@RequestParam("file")MultipartFile file,
                                          @RequestParam("group")String group,
                                          @RequestParam("description")String description,
                                          @RequestParam(value="id",required = false)Integer id){
        Map<String,Object> map=new HashMap<>();

        //验证文件是否为空
        if(file.isEmpty() || StringUtil.isNull(file.getOriginalFilename())){
            map.put("result",0);
            map.put("message","图片为空！");
            return map;
        }

        //验证是否图片
        if(!ImageUtil.isImage(file)){
            map.put("result",0);
            map.put("message","文件格式错误！");
            return map;
        }

        /*保存图片*/
        //保存到数据库
            //添加
        Image image=null;
        if (null == id||id.equals("")||id<0) {
            logger.info("添加图片：name=" + file.getOriginalFilename() + ",type=" + file.getContentType());
            image=new Image();
        }
            //更新
        else if (id > 0) {
            logger.info("更新图片：id=" + id + "name=" + file.getOriginalFilename() + ",type=" + file.getContentType());
            image=imageRepository.findOne(id);
            if(image==null){
                map.put("result", 0);
                map.put("message", "更新失败，请确认图片是否存在！");
                return map;
            }
        } else {
            map.put("result", 0);
            map.put("message", "参数错误！");
            return map;
        }
        //获取当前用户
        Users user=usersService.getCurrentUser();
        String userName=user.getUsername();
        int userId=usersRepository.findByUsername(userName).getId();
        //保存数据
        if(description ==null || description.length()>50){
            map.put("result", 0);
            map.put("message", "请填写50字内的图片描述！");
            return map;
        }
        image.setDescription(description);
        image.setCreateDate(new Date());
        image.setAuthor(userName);
        image.setAuthorId(userId);
        image.setModel(group);
        image.setOriginalName(file.getOriginalFilename());

        //获取数据库生成的id，用于生成唯一命名
        image = imageRepository.save(image);

        //保存到项目
        String path;
            //更新操作应当先删除原有图片
        if(id!=null&&id>0){
            path=image.getPath();
            //删除图片
            try {
                imageService.deleteResource(path);
            } catch (FileNotFoundException e) {
                map.put("result",0);
                map.put("message","项目中不存在该图片！");
                return map;
            } catch (BusinessException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message","删除原图片失败！");
                return map;
            }
        }
        try {
            //返回图片路径
            path=imageService.saveResource(file,image);
        } catch (BusinessException e) {
            e.printStackTrace();
            map.put("result",0);
            map.put("message","上传失败，图片格式错误！");
            return map;
        }
        image.setPath(path); //保存图片路径
        imageRepository.save(image);
        return map;
    }

    /**
     * 删除图片
     */
    /**存在问题：当删除不存在路径的图片记录时，无法成功删除记录*/
    @RequestMapping("/delete")
    @Transactional
    public Map<String,Object> deleteImage(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();

        logger.info("删除图片：id="+id);

        Image image=null;
        if(id != null && id>0){ //验证参数部分放在controller层，放在service层则删除项目文件、数据库记录分别需要一次验证，更复杂
            //删除项目文件
            image=imageRepository.findOne(id);
            if(image==null){
                map.put("result",0);
                map.put("message","找不到该图片！");
                return map;
            }
            try {
                imageService.deleteResource(image.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message","找不到该图片！");
                return map;
            } catch (BusinessException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message",e.getMessage());
                return map;
            }
            //删除数据库记录
            imageRepository.delete(id);
        }
        logger.info("--删除图片"+image.getPath()+"成功--");
        map.put("result",1);
        map.put("message","删除成功！");
        return map;
    }

    /**
     * 查找图片
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Map<String,Object> page(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置 ，如第10条
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        int size = Integer.parseInt(pageSize);
        //模糊|高级查询标志
        String fuzzy = request.getParameter("fuzzySearch");
        if(draw==null||startIndex==null||pageSize==null||fuzzy==null){
            map.put("result", 0);
            map.put("message", "参数不完整！");
            return map;
        }
        //页码
        int currentPage = Integer.parseInt(startIndex)/size+1;
        //当前用户
        Users user=usersService.getCurrentUser();

        logger.info("size = "+size+",currentPage = "+currentPage);

        Page<Image> imagePage = null;
        //模糊查找
        if("true".equals(fuzzy)){
            String searchValue=request.getParameter("fuzzy");

            //空搜
            if(StringUtil.isNull(searchValue)){
                //imagePage=imageService.selectByRole(user,currentPage-1,size);
                logger.debug("空搜 StringUtil.isNull(searchValue)");
                imagePage=imageService.selectByRole(user,currentPage-1,size,imageRepository);
            }

            else {
                Pageable pageable = new PageRequest(currentPage - 1, size, Sort.Direction.DESC, "createDate");
                imagePage = imageRepository.fuzzySearch(searchValue, pageable);
            }
        }
        //高级查找
        else{
            String author=request.getParameter("author");
            String description=request.getParameter("description");
            String model=request.getParameter("model");
            String firstDate1=request.getParameter("firstDate");
            String lastDate1=request.getParameter("lastDate");
            //字符串对象转为日期对象
            Date firstDate=null;
            Date lastDate=null;
            try {
                if(StringUtil.isNull(firstDate1)){
                    firstDate=new SimpleDateFormat("yyyy-MM-dd").parse(firstDate1);
                }
                if(StringUtil.isNull(lastDate1)) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                map.put("draw", draw);
                map.put("result", 0);
                map.put("message", "日期格式错误！");
                return map;
            }

            //空搜
            if(StringUtil.isNull(author)&&StringUtil.isNull(description)&&StringUtil.isNull(model)&&StringUtil.isNull(firstDate1)&&StringUtil.isNull(lastDate1)){
                imagePage=imageService.selectByRole(user,currentPage-1,size,imageRepository);
            }

            //以下代码无法实现权限控制
            else{
                UsefulImage image=new UsefulImage(user.getUsername(), model, description, firstDate, lastDate);
                imagePage = imageService.activeSearch(image,currentPage-1,size,imageRepository);
            }

        }

        //总记录数
        long total = imagePage.getTotalElements();
        logger.info("total="+total);

        map.put("draw", draw);
        map.put("result", 1);
        if(0==total){
            map.put("message", "未查询到图片！");
        }else {
            map.put("total", total);
            map.put("pageData", imagePage.getContent());
            map.put("message", "成功获取分页数据！");
        }
        return map;
    }

    /**
     * 获取图片链接
     **/
    @RequestMapping("/getPath")
    public Map<String,Object> loadImage(@RequestParam("id")Integer id,HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        logger.info("获取图片链接：id="+id);

        if(null==id||id<=0){
            map.put("result",0);
            map.put("message","参数错误！");
            return map;
        }

        //获取图片
        Image image=imageRepository.findOne(id);
        if(image==null){
            map.put("result",0);
            map.put("message","找不到该图片！");
        }else{ //获取图片输出流
            try {
                imageService.getOutputStream(image.getPath(),response);
            } catch (FileNotFoundException e) {
                map.put("result",0);
                map.put("message","找不到该图片！");
                return map;
            } catch (IOException e) {
                map.put("result",0);
                map.put("message","格式错误！");
                return map;
            }
            logger.info("成功获取图片链接--项目地址:"+image.getPath());
            map.put("result",1);
            map.put("message","成功获取图片！");
        }
        return map;
    }

    //未成功！改用配置静态资源的方式了
    //http://localhost:8080/imag/getImageById.do?id=10
    @RequestMapping(value = "/getImageById")
    @ResponseBody
    public String getImageById(HttpServletRequest request, HttpServletResponse response,Model model
            ,@RequestParam("id")int id) {
        Image image = imageRepository.findOne(id);
        if(image==null){
            return "error!";
        }
        String imgPath = image.getPath();
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            //String path1 = System.getProperty("user.dir");
            fis = new FileInputStream(imgPath);
            os = response.getOutputStream();
            response.setHeader("Content-type","image/png"); //设置响应内容类型(不管图片是什么格式都能正确显示)
            int count = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((count = fis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }try {
            fis.close();
            os.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return "ok";
    }
}