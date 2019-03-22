package llcweb.com.service.impl;

import llcweb.com.dao.repository.SoftwareRepository;
import llcweb.com.domain.models.Software;
import llcweb.com.service.SoftwareService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class SoftwareServiceImplTest {

    @Resource
    private SoftwareRepository softwareRepository;
    @Resource
    private SoftwareService softwareService;

    /**
     * @Author haien
     * @Description 根据标题、申请日期、简介、作者列表、申请号、公开号、公开日期和申请人动态查询
     * @Date 2018/10/10
     * @Param [spec, pageNum, pageSize]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Software>
     **/
    @Test
    public void activeSearchTest() throws ParseException {
        Software software=new Software();
        Date publicDate=new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-02");
        software.setPublicDate(publicDate);
        Page<Software> softwarePage=softwareService.activeSearch(software,0,10);
        Assert.assertThat(softwarePage.getTotalElements(),is(2L));
    }

}