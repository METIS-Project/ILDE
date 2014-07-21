<?php
/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/
extract($vars);

?>

<div id="content_area_user_title">
    <h2><?php echo T("Tracking data") ?></h2>
</div>


<pre>
Start:  <span friendly_timestamp="<?php echo $start?>"></span> UTC
End:    <span friendly_timestamp="<?php echo $end?>"></span> UTC

<a href="<?php echo $url ."pg/lds/tracking/user_conceptualize_tool/" . $start . "/" . $end;?>">
    Conceptualize functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_authoring_tool/" . $start . "/" . $end;?>">
    Author functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_implement/" . $start . "/" . $end;?>">
    Implement functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_project/" . $start . "/" . $end;?>">
    Project functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_browsing/" . $start . "/" . $end;?>">
    Browsing functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_sharing/" . $start . "/" . $end;?>">
    Sharing functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_conceptualize_tool_saved/" . $start . "/" . $end;?>">
    Non saved conceptualize functions
</a>
<a href="<?php echo $url ."pg/lds/tracking/user_authoring_tool_saved/" . $start . "/" . $end;?>">
    Non saved authoring functions
</a>


<a href="<?php echo $url ."pg/lds/tracking/tool";?>">Number of designs created with each tool</a>


<a href="<?php echo $url ."pg/lds/tracking/tool";?>">Number of designs created with each tool</a>
<a href="<?php echo $url ."pg/lds/tracking/user_tool";?>">Number and list of designs created with each tool by each user</a>

List of <a href="<?php echo $url ."pg/lds/tracking/iduser";?>">users</a>/<a href="<?php echo $url ."pg/lds/tracking/idgroup";?>">groups</a>/<a href="<?php echo $url ."pg/lds/tracking/idlds";?>">LdS synoptic table</a> with id, name and tool.

Produce a LD
<a href="<?php echo $url ."pg/lds/tracking/created_by_user";?>">Number of designs produced by each user</a>
Number of designs created / modified by days/weeks/months
List of designs modified by each user by <a href="<?php echo $url ."pg/lds/tracking/userdays";?>">days</a>/<a href="<?php echo $url ."pg/lds/tracking/userweeks";?>">weeks</a>/<a href="<?php echo $url ."pg/lds/tracking/usermonths";?>">months</a>

<a href="<?php echo $url ."pg/lds/tracking/ldsedits";?>">Co-produce a LD</a>
Number of reviews (edits) to a design (in global or by users)
Number of users editing a design
List of users editing a design

<a href="<?php echo $url ."pg/lds/tracking/ldssharing";?>">Share a LD</a>
Number of users with whom a design has been shared with editing right
Number of users with whom a design has been shared with view right
users with whom a design has been shared with editing right
users with whom a design has been shared with view right
Number of designs shared with others with editing rights
List of designs shared with others with editing rights

<a href="<?php echo $url ."pg/lds/tracking/ldsprivate";?>">List of private designs</a>
Number of private designs (no shared, no view / editing rights for others)


<a href="<?php echo $url ."pg/lds/tracking/ldsimplemented";?>">Instantiated (implemented) learning designs</a>
<a href="<?php echo $url ."pg/lds/tracking/implementations";?>">Implementations</a>
<a href="<?php echo $url ."pg/lds/tracking/deployments";?>">Deployments</a>
Number of designs associated to at least a VLE
List of designs associated to at least a VLE by implementer
Number of implementations (design associated to VLEs) for a design

Deploy an instantiated LD (2nd prototype)
Number of times a deployment package is created for a design (to be coordinated with UVa-GLUEPS)

<a href="<?php echo $url ."pg/lds/tracking/ldscomments";?>">Provide feedback on a LD</a>
Number of comments associated to each design by commenter

<a href="<?php echo $url ."pg/lds/tracking/ldsviewed";?>">Explore LDs</a>
Number of times a user viewed a design (by user and by design)
Number of times a design has been viewed by user and by design)


Number of members in the community: <?php echo $nUsers;?>


-<a href="<?php echo $url ."pg/lds/tracking/groups";?>">Groups</a>
Number of groups
Number of members in each group

Number of designs published (accessible outside LdShake): <?php echo $nPublished;?>


<a href="<?php
    if($url == "http://web.dev/ilde/")
        echo 'https://www.google.com/';
    if($url == "http://ilde.upf.edu/")
        echo 'https://www.google.com/analytics/web/?hl=es&pli=1#report/visitors-overview/a40421532w69605041p71724745/%3F_u.date00%3D20130903%26_u.date01%3D20130903/';
    if($url == "http://ilde.upf.edu/agora/")
        echo 'https://www.google.com/analytics/web/?hl=es&pli=1#report/visitors-overview/a40421532w69605041p76205020/%3F_u.date00%3D20130903%26_u.date01%3D20130903/';
    if($url == "http://ilde.upf.edu/kek/")
        echo 'https://www.google.com/analytics/web/?hl=es&pli=1#report/visitors-overview/a40421532w69605041p76202644/%3F_u.date00%3D20130903%26_u.date01%3D20130903/';
    if($url == "http://ilde.upf.edu/ou/")
        echo 'https://www.google.com/analytics/web/?hl=es&pli=1#report/visitors-overview/a40421532w69605041p76200864/%3F_u.date00%3D20130903%26_u.date01%3D20130903/';
?>">Visits to the site / pages (request access to the data sending an email to pablo.abna at gmail.com with a google enabled account)</a>

Total number of comments: <?php echo count_annotations(0, 'object', 'LdS', 'generic_comment');?>

Total number of LdS: <?php echo (get_entities('object', 'LdS', 0, '', 99999, 0, true)-$nUsers);?>


</pre>