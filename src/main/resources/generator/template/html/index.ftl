<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="top"></div>
<!--导航 Start-->
<div class="menu">
    <div class="all-sort">
        <h2>
            <a href="">${alltype}</a>
        </h2>
    </div>
    <div class="ad">
        <a href=""> <img
                    src="images/rBEhVlJEA84IAAAAAAAZY9Mm2-UAADmFQNaVy4AABl7123.jpg"
                    width="141" height="40" /> </a>
    </div>
    <div class="nav">
        <ul class="clearfix">
            <li><a href="http://sc.chinaz.com" class="current">${index}</a>
            </li>
            <li><a href="http://sc.chinaz.com">${dress}</a>
            </li>
            <li><a href="http://sc.chinaz.com">${supermarket}</a>
            </li>
            <li><a href="http://sc.chinaz.com">${teamBuy}</a>
            </li>
            <li><a href="http://sc.chinaz.com">${auction}</a>
            </li>
            <li><a href="http://sc.chinaz.com">${OnlineGame}</a>
            </li>
        </ul>
    </div>
</div>
<!--导航 End-->

<!--所有分类 Start-->
<#list goodsCetegory as goodsCetegory >
<div class="wrap">
    <div class="all-sort-list">
        <div class="item bo">
            <h3><span>·</span><a href="">${goodsCetegory.name}</a><!-- <a href="">图书</a>、<a href="">音像</a>、<a href="">数字商品</a> --></h3>
            <div class="item-list clearfix">
                <div class="close">x</div>
                <div class="subitem">
                    <#list goodsTwoCetegory as goodsTwoCetegory >
                        <dl class="fore1">
                            <#if (goodsTwoCetegory.pid == goodsCetegory.id ) >
                                <dt><a href="#">${goodsTwoCetegory.name }</a></dt>
                                <#list goodsThreeeCetegory as goodsThreeeCetegory >
                                    <#if (goodsThreeeCetegory.pid == goodsTwoCetegory.id) >
                                        <dd><em><a href="#">${goodsThreeeCetegory.name }</a></em></dd>
                                    </#if>
                                </#list>
                            </#if>
                        </dl>
                    </#list>
                </div>
                <!---------------------------------------------循环体-------------------------------------------------------->
                <div class="cat-right">
                    <dl class="categorys-promotions"
                        clstag="homepage|keycount|home2013|0601c">
                        <dd>
                            <ul>
                                <li><a href="#" target="_blank"><img
                                                src="images/rBEhWFJTydgIAAAAAAAiD8_1J3AAAD5mAMC0SYAACIn230.jpg"
                                                width="194" height="70" title="特价书满减"> </a>
                                </li>
                                <li><a href="#" target="_blank"><img
                                                src="images/rBEhVlJTyt8IAAAAAAAiq1D-0D8AAD7_gIE2KUAACLD102.jpg"
                                                width="194" height="70" title="重磅独家"> </a>
                                </li>
                            </ul>
                        </dd>
                    </dl>
                    <dl class="categorys-brands"
                        clstag="homepage|keycount|home2013|0601d">
                        <dt>推荐品牌出版商</dt>
                        <dd>
                            <ul>
                                <li><a href="#" target="_blank">中华书局</a>
                                </li>
                                <li><a href="#" target="_blank">人民邮电出版社</a>
                                </li>
                                <li><a href="#" target="_blank">上海世纪出版股份有限公司</a>
                                </li>
                                <li><a href="#" target="_blank">电子工业出版社</a>
                                </li>
                                <li><a href="#" target="_blank">三联书店</a>
                                </li>
                                <li><a href="#" target="_blank">浙江少年儿童出版社</a>
                                </li>
                                <li><a href="#" target="_blank">人民文学出版社</a>
                                </li>
                                <li><a href="#" target="_blank">外语教学与研究出版社</a>
                                </li>
                                <li><a href="#" target="_blank">中信出版社</a>
                                </li>
                                <li><a href="#" target="_blank">化学工业出版社</a>
                                </li>
                                <li><a href="#" target="_blank">北京大学出版社</a>
                                </li>
                            </ul>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
    </#list>
</div>
<!--所有分类 End-->
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">
    $('.all-sort-list > .item').hover(
        function() {
            var eq = $('.all-sort-list > .item').index(this), //获取当前滑过是第几个元素
                h = $('.all-sort-list').offset().top, //获取当前下拉菜单距离窗口多少像素
                s = $(window).scrollTop(), //获取游览器滚动了多少高度
                i = $(this).offset().top, //当前元素滑过距离窗口多少像素
                item = $(this).children('.item-list').height(), //下拉菜单子类内容容器的高度
                sort = $('.all-sort-list').height(); //父类分类列表容器的高度

            if (item < sort) { //如果子类的高度小于父类的高度
                if (eq == 0) {
                    $(this).children('.item-list').css('top', (i - h));
                } else {
                    $(this).children('.item-list').css('top',
                        (i - h) + 1);
                }
            } else {
                if (s > h) { //判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                    if (i - s > 0) { //则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                        $(this).children('.item-list').css('top',
                            (s - h) + 2);
                    } else {
                        $(this).children('.item-list').css('top',
                            (s - h) - (-(i - s)) + 2);
                    }
                } else {
                    $(this).children('.item-list').css('top', 3);
                }
            }

            $(this).addClass('hover');
            $(this).children('.item-list').css('display', 'block');
        }, function() {
            $(this).removeClass('hover');
            $(this).children('.item-list').css('display', 'none');
        });

    $('.item > .item-list > .close').click(function() {
        $(this).parent().parent().removeClass('hover');
        $(this).parent().hide();
    });
</script>
<div style="text-align:center;clear:both"></div>
</body>
</html>
