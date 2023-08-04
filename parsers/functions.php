<?php
function convertMYsqliToIntArray($mysqli){
    return mysqli_fetch_array($mysqli, MYSQLI_NUM);
}
function deleteAllFiles($directory){
            $directory = $directory;
            $files = glob($directory.'\*'); 
            foreach($files as $file) 
                   if(is_file($file))
                        unlink($file); 
    }   
function convertMysqliToString($msqli){
        $msqli =  $msqli -> fetch_array();
        return strval($msqli[0]);
    }
    function convertMysqliToInt($mysqli){
        $mysqli = $mysqli->fetch_array();
        return intval($mysqli[0]); 
    }
    function clean($string,$cn){
        $string = mysqli_real_escape_string($cn,$string);
        return str_replace(array(")","(","'",'"'),"",$string);
    }
    function getHtml($url){
        $ch = curl_init($url);//запускаем и настраиваем парсер
        $config['useragent'] = 'Mozilla/5.0 (Windows NT 6.2; WOW64; rv:17.0) Gecko/20100101 Firefox/17.0';
        curl_setopt($ch, CURLOPT_USERAGENT, $config['useragent']);
        curl_setopt($ch, CURLOPT_REFERER, 'https://www.bibliofond.ru');
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
        curl_setopt($ch,CURLOPT_HEADER,true);
        curl_setopt($ch,CURLOPT_HTTPHEADER, array("Accept-Charset: UTF-8"));
        $html = curl_exec($ch);
        curl_close($ch);
        return $html;
   }
   function getFullLink($a,$domain){           
       $link =$a->href;
       return $domain.$link;                         
    }
   function findLinksInArray($array,$domain){ //находим в каждом из элементов массива линки
       $linkArray = [];
      foreach($array as $element){
          $html = str_get_html($element);
          $as = $html->find("a[href]");
          foreach ($as as $a){
            $link = getFullLink($a,$domain);
            array_push($linkArray,$link);
          } 
       }
       return $linkArray;
   } 
   function findLinksInElement($element,$domain){
       $linkArray = [];
           $html = str_get_html($element);
           $as = $html->find("a[href]");
           foreach ($as as $a){
             $link = getFullLink($a,$domain);
             array_push($linkArray,$link);
           } 
        return $linkArray;

   }
   function showArray($array){
       foreach($array as $element)
           echo $element."<br/>";
   }
  
   

?>