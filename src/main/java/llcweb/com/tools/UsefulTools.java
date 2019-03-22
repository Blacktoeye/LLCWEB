package llcweb.com.tools;/**
 * Created by:Ricardo
 * Description:
 * Date: 2019/1/17
 * Time: 22:59
 */

import llcweb.com.domain.entities.ProductInfo;
import llcweb.com.domain.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName: UsefulTools
 *@Description: TODO
 *@Author: Ricardo
 *@Date: 2019/1/17 22:59
 **/
public class UsefulTools {

    static public List<ProductInfo> paperToProductInfo(List<Paper> patentList){
        List<ProductInfo>productInfoList = new ArrayList<>();

        for (Paper patent:patentList){
            productInfoList.add(new ProductInfo(patent.getId(),patent.getTitle(),patent.getDate(),
                    patent.getIntroduction(),patent.getAuthorList(),patent.getState()));
        }
        return productInfoList;
    }
   static public List<ProductInfo> patentToProductInfo(List<Patent> patentList){
       List<ProductInfo>productInfoList = new ArrayList<>();

       for (Patent patent:patentList){
           productInfoList.add(new ProductInfo(patent.getId(),patent.getTitle(),patent.getPublicDate(),
                   patent.getIntroduction(),patent.getAuthorList(),patent.getState()));
       }
       return productInfoList;
   }

    static public List<ProductInfo> softwareToProductInfo(List<Software> patentList){
        List<ProductInfo>productInfoList = new ArrayList<>();

        for (Software patent:patentList){
            productInfoList.add(new ProductInfo(patent.getId(),patent.getTitle(),patent.getPublicDate(),
                    patent.getIntroduction(),patent.getAuthorList(),patent.getState()));
        }
        return productInfoList;
    }

    static public List<ProductInfo> projectToProductInfo(List<Project> patentList){
        List<ProductInfo>productInfoList = new ArrayList<>();

        for (Project patent:patentList){
            productInfoList.add(new ProductInfo(patent.getId(),patent.getProjectName(),patent.getStartDate(),
                    patent.getResearchField(),patent.getMembers(),patent.getStatus()));
        }
        return productInfoList;
    }

    static public List<ProductInfo> activityToProductInfo(List<Activity> patentList){
        List<ProductInfo>productInfoList = new ArrayList<>();

        for (Activity patent:patentList){
            productInfoList.add(new ProductInfo(patent.getId(),patent.getTitle(),patent.getStartDate(),
                    patent.getAuthor(),patent.getPeopleList(),patent.getActivityType()));
        }
        return productInfoList;
    }
}
