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
<a href="<?php echo $url ."pg/lds/tracking/tool";?>">Number and list of designs created with each tool by each user</a>

Produce a LD
Number of designs produced by each user
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

Number of private designs (no shared, no view / editing rights for others)
<a href="<?php echo $url ."pg/lds/tracking/ldsprivate";?>">List of private designs</a>


<a href="<?php echo $url ."pg/lds/tracking/ldsimplemented";?>">Instantiate (Implement) a LD</a>
Number of designs associated to at least a VLE
List of designs associated to at least a VLE by implementer
Number of implementations (design associated to VLEs) for a design

Deploy an instantiated LD
Number of times a deployment package is created for a design (to be coordinated with UVa-GLUEPS)

<a href="<?php echo $url ."pg/lds/tracking/ldscomments";?>">Provide feedback on a LD</a>
Number of comments associated to each design by commenter

<a href="<?php echo $url ."pg/lds/tracking/ldsviewed";?>">Explore LDs</a>
Number of times a user viewed a design (by user and by design)
Number of times a design has been viewed by user and by design)


Number of members in the community
Number of groups
Number of members in each group
Number of designs published (accessible outside LdShake)
Visits to the site / pages
Number of designs created with each tool

</pre>