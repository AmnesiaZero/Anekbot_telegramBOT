<?php
include "simple_html_dom.php";
include "functions.php";
set_time_limit(0);
if(parser()==true) echo "Программа выполнена успешно";
function parser(){
    include "config.php";
    $url = "https://www.anekdot.ru/tags/";
    $cn = mysqli_connect($dbHost,$dbUser,$dbPassword,$dbName);
    $html = str_get_html(getHtml($url));
    $div = $html -> find("div.tags-cloud");
    $aArray = $div[0]->find("a[href]");
    foreach($aArray as $a){
        $theme = $a->getAttribute('title');
        $theme = clean($theme,$cn);
        mysqli_query($cn,"INSERT INTO `anekdot_themes`(`id`, `theme`) VALUES (NULL,'$theme')");
        $linkToCategory = getFullLink($a,"https://www.anekdot.ru");
        parseCategory($linkToCategory,$theme,$cn);
    }
    return true;
}
function parseCategory($url,$theme,$cn){
    echo "Категория = $theme";
    $flag = true;
    $themeId = convertMysqliToInt(mysqli_query($cn,"SELECT MAX(id) FROM `anekdot_themes`"));
    $html = str_get_html(getHtml($url));
    $counter = 1;
    while($flag){
        $texts = $html -> find("div.text");
        foreach($texts as $text){
            $text = str_replace("<br>","\n",$text);
            $text = clean(strip_tags($text),$cn);
            $sql = "INSERT INTO `anekdot_store`(`id`, `theme_id`, `text`) VALUES (NULL,$themeId,'$text')";
            echo "Тема - $theme,анекдот номер - $counter////";
            mysqli_query($cn,$sql);
            $counter++;
        }
        $div = $html -> find("div.pageslist");
        $aArray = str_get_html($div[0]) -> find("a[href]");
        $lastButtonText = strip_tags(end($aArray));
        echo $lastButtonText."///////";
        if(strpos($lastButtonText,"след") !== false){
            echo "Найдена следующая страница///";
            $nextPageUrl = getFullLink(end($aArray),"https://www.anekdot.ru");
            $html = str_get_html(getHtml($nextPageUrl));
        }
        else {
            echo "Страница не найдена/////";
            $flag = false;
        }
    }
}
?>