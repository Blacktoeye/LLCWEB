package llcweb.com.dao.repository;

import llcweb.com.domain.models.Software;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoftwareRepositoryTest {
    @Resource
    private SoftwareRepository softwareRepository;

    @Test
    public void fuzzySearchTest(){
        Pageable pageable=new PageRequest(0,10);
        Page<Software> softwarePage=softwareRepository.fuzzySearch("aaaaa1",pageable);
        Assert.assertThat(softwarePage.getTotalElements(),is(3L));
    }

    @Test
    public void getSoftwaresTest(){
        List<Software> softwares=softwareRepository.getLatest(4);
        for (Software software:softwares){
            System.out.println(software.getTitle());
        }
    }
}