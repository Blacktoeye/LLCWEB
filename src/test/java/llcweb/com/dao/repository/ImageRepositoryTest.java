package llcweb.com.dao.repository;

import llcweb.com.domain.entity.UsefulImage;
import llcweb.com.domain.models.Image;
import llcweb.com.service.ImageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * @Author haien
 * @Description TODO
 * @Date 16:23 2018/8/22
 * @Param
 * @return
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;
    private ImageService imageService;

    private Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2018-08-22");
    private Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2018-08-25");
    public ImageRepositoryTest() throws ParseException {
    }

    private String people = "haien1";
    private String title = "第8次";
    private String description = "项目组";

    @Test
    public void add() {
        Image image = new Image();
        image.setId(1);
        image.setAuthor("haien");
        image.setDescription("项目组第1次会议");
        image.setCreateDate(new Date());
        Assert.assertThat(imageRepository.save(image).getId(), is(1));
    }

    @Test
    public void findAll() throws ParseException {
        UsefulImage image=new UsefulImage();
        image.setFirstDate(date1);
        image.setLastDate(date2);
        //document.setAuthor("haien2");
        Page<Image> images= imageService.activeSearch(image,1,3,imageRepository);
        Assert.assertThat(images.getTotalElements(),is(4L));
    }

    @Test
    public void fuzzySearch(){
        Page<Image> imageList=imageRepository.fuzzySearch("haien",new PageRequest(0,10, Sort.Direction.DESC,"date"));
        Assert.assertThat(imageList.getTotalElements(),is(10L));
    }

    @Test
    public void findByModel(){
        Page<Image> imageList=imageRepository.findByModel("数据组",new PageRequest(0,15));
        System.out.println(imageList.getTotalElements());
        Assert.assertThat(imageList.getTotalElements(),greaterThan(3L));
        for (Image image: imageList){
            System.out.println(image);
        }
    }

}
