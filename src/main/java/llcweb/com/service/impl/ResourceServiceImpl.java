package llcweb.com.service.impl;

import llcweb.com.dao.repository.myInterface.ResourceRepository;
import llcweb.com.domain.entity.Resource;
import llcweb.com.domain.entity.UsefulResource;
import llcweb.com.domain.models.Image;
import llcweb.com.domain.models.Roles;
import llcweb.com.domain.models.Users;
import llcweb.com.exception.BusinessException;
import llcweb.com.exception.ReturnCode;
import llcweb.com.service.ResourceService;
import llcweb.com.tools.StringUtil;
import llcweb.com.tools.UUIDUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl<T> implements ResourceService<T> {

    @Value("${image.location}")
    private String imagePath;
    @Value("${file.location}")
    private String filePath;

    @Override
    public Page<T> activeSearch(UsefulResource resource, int pageNum, int pageSize,
                                    ResourceRepository resourceRepository) {

        //规格定义
        Specification<T> specification =new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                //添加断言
                if (!StringUtil.isNull(resource.getAuthor())) {
                    Predicate like = cb.equal(root.get("author").as(String.class),resource.getAuthor());
                    predicates.add(like);
                }
                if (resource.getFirstDate() != null) {
                    Predicate greaterThanOrEqualTo = cb.greaterThanOrEqualTo(root.get("createDate"), resource.getFirstDate());
                    predicates.add(greaterThanOrEqualTo);
                }
                if (resource.getLastDate() != null) {
                    Predicate lessThanOrEqualTo = cb.lessThanOrEqualTo(root.get("createDate"), resource.getLastDate());
                    predicates.add(lessThanOrEqualTo);
                }
                if (!StringUtil.isNull(resource.getTitle())) {
                    Predicate like = cb.like(root.get("title"), "%" + resource.getTitle() + "%");
                    predicates.add(like);
                }
                if (!StringUtil.isNull(resource.getModel())) {
                    Predicate like = cb.like(root.get("model"), "%" + resource.getModel() + "%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(resource.getInfo())){
                    Predicate like = cb.like(root.get("infor"),"%"+resource.getInfo()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(resource.getDescription())){
                    Predicate like = cb.like(root.get("description"),"%"+resource.getDescription()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(resource.getIntroduction())){
                    Predicate like = cb.like(root.get("introduction"),"%"+resource.getIntroduction()+"%");
                    predicates.add(like);
                }

                //将List转换为数组
                return cb.and(predicates.toArray(new Predicate[0])); // toArray(数组)：将调用方法的对象转换为数组，
                                                                     // 并存储到传入的数组中；若数组空间不足则重新创造一个数组并返回
            }

        };

        //按时间排序
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"createDate");

        //查询
        return resourceRepository.findAll(specification,pageable);
    }

    /**
     * @Author haien
     * @Description 根据用户权限查找
     * @Date 2018/9/21
     * @Param [user, pageNum, pageSize, resourceRepository]
     * @return org.springframework.data.domain.Page<T>
     **/
    @Override
    public Page<T> selectByRole(Users user, int pageNum, int pageSize,
                                ResourceRepository resourceRepository) {
        Page<T> resources;
        List<Roles> roles=user.getRoles();
        Pageable page=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"createDate");

        //管理员查看所有文档
        for(Roles role:roles){
            if(role.getrFlag().equals("ADMIN")){
                resources=resourceRepository.findAll(page);
                return resources;
            }
        }

        //组长查看本组文档
        for(Roles role:roles){
            if(role.getrFlag().equals("GROUP")){
                resources=resourceRepository.findByModel("user.getModel()",page);
                return resources;
            }
        }

        //普通用户查找编辑过的文档
        System.out.println("user = "+user.getUsername());
        resources=resourceRepository.findByAuthorId(user.getId(),page);
        System.out.println("find number = "+resources.getTotalElements());
        return resources;
    }

    /**
     * @Author haien
     * @Description 保存文件到项目
     * @Date 2018/9/21
     * @Param [multipartFile, resource]
     * @return java.lang.String
     **/
    @Override
    public String saveResource(MultipartFile multipartFile, Resource resource) throws BusinessException {

        String originalFileName=multipartFile.getOriginalFilename();
        String suffix=originalFileName.substring(originalFileName.lastIndexOf("."));
        //选择路径
        String path=filePath;
        if(resource instanceof Image){
            path=imagePath;
        }
        //拼接文件名
        String filePath=path+"/"+UUIDUtil.getUUID()+suffix;
        try {
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(filePath));
            //可以直接获取MultipartFile的字节数组，这样就省了先获取InputStream再用一个1024的字节数组去read出来的麻烦
            //重点是FileInputStream fis=(FileInputStream)file.getInputStream()抛异常：
            //java.lang.ClassCastException: java.io.ByteArrayInputStream cannot be cast to java.io.FileInputStream
            byte[] flush=multipartFile.getBytes();
            //已转为字节数组，可一次性写出
            bos.write(flush);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ReturnCode.CODE_FAIL, "格式错误！");
        }
        return filePath;
    }

    /**
     * @Author haien
     * @Description 客户端获取文件输出流
     * @Date 2018/9/21
     * @Param [path, response]
     * @return void
     **/
    @Override
    public void getOutputStream(String path, HttpServletResponse response) throws IOException {
        byte[] buff = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        OutputStream os = response.getOutputStream(); //服务器向浏览器发送字节输出流
        response.setHeader("Content-type","image/png"); //设置响应内容类型(虽然设置的是png,但是连gif也可以正常显示)
        int len = 0;
        while ((len = bis.read(buff)) != -1) {
            os.write(buff, 0, len);
        }
        os.flush();
        bis.close();
    }

    /**
     * @Author haien
     * @Description 删除项目中指定图片
     * @Date 2018/9/17
     * @Param [path]
     * @return void
     **/
    @Override
    public void deleteResource(String path) throws FileNotFoundException, BusinessException {
        if (path==null||path.isEmpty()){
            return;
        }
        File file=new File(path);//路径为空直接报错了
        //路径为文件且不为空则删除
        if(file.isFile()&&file.exists()){
            file.delete();
        }else if(!file.isFile()){
            throw new BusinessException(ReturnCode.CODE_FAIL,"路径非文件");
        }else{
            throw new FileNotFoundException();
        }
    }

}
