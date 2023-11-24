//package com.geekplus.common.util.image;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 11/19/23 13:09
// * description: 做什么的？icepdf实现缩略图展示
// */
//public class CapturePdfFirstPageUtil {
//
//    /**
//     * 生成一本书的缩略图
//     * @param inputFile        需要生成缩略图的书籍的完整路径
//     * @param outputFile    生成缩略图的放置路径
//     */
//    public static void generateBookIamge(String inputFile, String outputFile) {
//        Document document = null;
//
//        try {
//            float rotation = 0f;
//            //缩略图显示倍数，1表示不缩放，0.5表示缩小到50%
//            float zoom = 0.8f;
//
//            document = new Document();
//            document.setFile(inputFile);
//            // maxPages = document.getPageTree().getNumberOfPages();
//
//            BufferedImage image = (BufferedImage)document.getPageImage(0, GraphicsRenderingHints.SCREEN,
//                    Page.BOUNDARY_CROPBOX, rotation, zoom);
//
//            Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
//            ImageWriter writer = (ImageWriter)iter.next();
//
//            FileOutputStream out = new FileOutputStream(new File(outputFile));
//            ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
//
//            writer.setOutput(outImage);
//            writer.write(new IIOImage(image, null, null));
//
//        } catch(Exception e) {
//            System.out.println( "to generate thumbnail of a book fail : " + inputFile );
//            System.out.println( e );
//        }
//    }
//
//    public static void main(String[]args){
//        CapturePdfFirstPageUtil.generateBookIamge("Users\\work\\Desktop\\spring2.pdf","C:\\Users\\work\\Desktop\\captureImage.jpg") ;
//    }
//}
