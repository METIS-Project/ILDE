<?php
include_once(__DIR__ . '/../../../../engine/start.php');
global $CONFIG;
$url = $CONFIG->url;
extract ($vars);
?><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>About ILDE</title>
<link rel="icon" type="image/png" href="<?php echo $url ?>_graphics/favicon.ico" />
<?php echo elgg_view('page_elements/analytics');?>
<style>
html, body
{
    height: 100%;
    width: 100%;
    margin: 0;
    font-family: Helvetica,Arial,sans-serif;
}

a
{
    outline: none;
}

h1, h2
{
    font-size: 19px;
    margin: 10px 0;
    /*text-align: right;*/
}

#about-text {
    text-align: left;
    width: 700px;
}

#about-topheader {
    font-size: 23px;
    color: #FFF;
    width: 800px;
    margin-left: auto;
    margin-right: auto;
}

#topbar
{
    background-image: url(<?php echo $url ?>_graphics/bg-header-big.png);
    height: 80px;
}

#loginform
{
    float: right;
    padding: 10px;
}

#middlecontents
{
    text-align: center;
}

#bottombar
{
    height: 75px;
    position: absolute;
    bottom: 0px;
    width: 100%;
}

#contentswrapper
{
    position: absolute;
    top: 00%;
    left: 50%;
    margin-top: 140px;/* half elements height*/
    margin-left: -350px;/* half elements width*/
}

#logo
{
    float: left;
    padding: 50px 30px 0 0;
    width: 400px;
}

#claims
{
    float: left;
    padding-left: 30px;
    border-left: 1px solid #dadbdc;
}

#claims
{
    text-align: left;
    width: 450px;
}

.home-pill
{
    float: left;
    margin: 15px;
    padding: 5px;
    text-align: center;
    font-weight: bold;
    width: 120px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
    -moz-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
    -webkit-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
    font-size: 14px;
}

.home-pill p
{
    margin: 3px 0;
}

.field
{
    float: left;
    margin-left: 15px;
}

label
{
    display: block;
    color: #C3DEC4;
    font-size: 11px;
    font-weight: bold;
}

input[type="submit"]
{
    margin: 15px 15px 0 0;
    display: block;
    float: right;
    border-radius: 3px 3px 3px 3px;
    -moz-border-radius: 3px 3px 3px 3px;
    -webkit-border-radius: 3px 3px 3px 3px;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
    background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
    border: 1px solid #ccc;
    color: #000;
}

.fieldoptions
{
    display: block;
    font-size: 12px;
    color: #C3DEC4;
}

.fieldoptions div
{
    margin-top: 8px;
    float: left;
}

input[type="checkbox"]
{
    margin: 8px 5px 0 0;
    display: block;
    float: left;
}

a.underfield
{
    margin-top: 8px;
    display: block;
    color: #C3DEC4;
}

#register
{
    font-weight: bold;
    font-size: 14px;
    color: #fff;
    float: right;
    margin: 30px 25px 0 0;
}

#register a
{
    color: #C3DEC4;
    font-weight: bold;
}

#about {
    margin-top: 7px;
}

#about a
{
    font-weight: bold;
    font-size: 14px;
    color: #fff;
}

#footer
{
    margin: 0 30px;
    border-top: 1px solid #dadbdc;
}

a img
{
    border: 0;
}

#footer-logos {
    height: 66px;
    /*width: 824px;*/
    float: left;
}

#footer-logos ul {
    list-style-image: none;
    list-style-type: none;
    list-style-position: outside;
    padding: 0;
    margin: 5px 0 0 0;
}

#footer-logos li {
    padding: 5px;
    float: left;
}

#footer-logos img {
    height: 57px;
}

#footer-contents
{
    margin: 16px 0 0 30px;
    float: left;
}

ul.links {
    list-style-image: none;
    list-style-type: none;
    list-style-position: outside;
    padding: 0;
    margin: 0 0 10px 0;
    font-size: 13px;
}

ul.links a
{
    color: #000;
}

.acknowledgements
{
    color: #777;
    font-size: 12px;
}

.messages
{
    background-color: #cfc;
    border: 1px solid green;
}

.messages_error
{
    background-color: #fcc;
    border: 1px solid red;
}

.messages,.messages_error
{
    font-size: 13px;
    margin: 3px;
    padding: 3px;
}

.messages p,.messages_error p
{
    margin: 0;
}

.messages a,.messages_error a
{
    display: none;
}
</style>
</head>
<body>
<div id="topbar">
    <br />
    <div id="about-topheader"><?php echo T("About ILDE") ?></div>
</div>
<?php echo elgg_view('messages/list', array('object' => $sysmessages)); ?>
<div id="middlecontents">
    <div id="contentswrapper">
        <div id="about-text">
        <?php echo elgg_view('lds/about.en'); ?>
        </div>
    </div>
</div>
<div id="bottombar">
    <div id="footer">
        <div id="footer-logos">
            <ul>
                <li><a href="http://metis-project.org/index.php" target="_blank"><img src="<?php echo $url ?>_graphics/metis/logo-footer-metis.png" alt="<?php echo T("METIS") ?>"></a></li>
                <li><a href="http://ec.europa.eu/education/lifelong-learning-programme/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/logos-footer-llp.png" alt="<?php echo T("Lifelong Learning Programme") ?>"></a></li>
                <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>
                <li><a href="http://www.gsic.uva.es/glueps/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/glueps-inside.png" alt="<?php echo T("GLUEPS") ?>"></a></li>
                <li><a href="http://www.gsic.uva.es/webcollage/index.php?l=en" target="_blank"><img src="<?php echo $url ?>_graphics/metis/WC-inside.png" alt="<?php echo T("WebCollage") ?>"></a></li>
                <li><a href="http://sourceforge.net/projects/openglm/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/openglm-supported.png" alt="<?php echo T("OpenGLM") ?>"></a></li>
                <li><a href="http://cosy.ds.unipi.gr/index.php?option=com_content&view=article&id=117&Itemid=71" target="_blank"><img src="<?php echo $url ?>_graphics/metis/cadmos-supported.png" alt="<?php echo T("CADMOS") ?>"></a></li>
                <li><a href="http://www.open.ac.uk/blogs/OULDI/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/ouldi-inside-supported.png" alt="<?php echo T("Ouldi tools") ?>"></a></li>
                <!--<li><a href="http://www.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/upf-logo.png" alt="<?php echo T("UPF logo") ?>"></a></li>
                    <li><a href="http://gti.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/gti-small.png" alt="<?php echo T("GTI UPF logo") ?>"></a></li>
                    <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>-->
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    document.getElementById("username").focus();
</script>
</body>
</html>