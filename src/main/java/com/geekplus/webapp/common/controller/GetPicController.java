/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/9 9:32 上午
 * description: 做什么的？
 */
package com.geekplus.webapp.common.controller;

import com.geekplus.common.domain.Result;
import com.geekplus.common.util.getpic.DownloadUtil;
import com.geekplus.common.util.getpic.UploadUtils;
import com.geekplus.common.util.getpic.GetUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GetPicController {
    @RequestMapping("/addReptile")
    public void Addreptile(@RequestBody List<String> picAdd){
        //String[] selected = request.getParameterValues("selected");
        //List<String> selecteds = Arrays.asList(selected);
        // 调用工具类将图片下载到本地 图片重命名
        for (String s : picAdd) {
            String uuidName = UploadUtils.getUUIDName(s);
            String filePath = "/Users/AppTest"+ uuidName;
            DownloadUtil.downloadImg(s, filePath);
        }
        //return Result.success();
    }

    @RequestMapping("/imgReptile")
    public Result ImgReptile(@RequestParam String webUrl){
        // 获取目标网址的主域名
        String firstUrl = GetUrl.getFirstUrl(webUrl);

        List<String> list = new ArrayList<String>();
        // 调用工具类
        String htmlSource = DownloadUtil.htmlSource(webUrl, "utf-8");
        // 获取图片url
        List<String> imageSrc = GetUrl.getImageSrc(htmlSource);
        for (int i = 0; i < imageSrc.size(); i++) {
            if (!imageSrc.get(i).contains("http://")) {
                list.add(firstUrl + imageSrc.get(i));
                continue;
            }
            list.add(imageSrc.get(i));
        }
        return Result.success(list);
        // 将list集合放在request
        //request.setAttribute("imageSrc", list);
        //request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
