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

$(document).ready(function () {
    $('#vle_delete').click(function() {
        $('#vle_delete_flag').val('1');
    });

    $('input[name=vle_password]').focus(function(){
        if($(this).val() == '000000')
            $(this).val('');
    });

    $('input[name=vle_submit]').click(function(e) {
        if($('input[name=vle_name]').val().length == 0
            || $('input[name=vle_type]').val().length == 0
            || $('input[name=vle_url]').val().length == 0
            || $('input[name=vle_username]').val().length == 0
            || $('input[name=vle_password]').val().length == 0
            ) {
            e.preventDefault();
            $('#vledata_submit_incomplete').show();
        }
    });

    $('input[name=vle_admin_submit]').click(function(e) {
        if($('input[name=vle_name]').val().length == 0
            || $('input[name=vle_type]').val().length == 0
            || $('input[name=vle_url]').val().length == 0
            ) {
            e.preventDefault();
            $('#vledata_submit_incomplete').show();
        }
    });

    $('select[name=vle_name_select]').change(function() {
        var selected_id = $(this).val();
        if(svlelist_data[selected_id]) {
            $('input[name=vle_name]').val(svlelist_data[selected_id].name);
            $('input[name=vle_type]').val(svlelist_data[selected_id].vle_type);
            $('input[name=vle_url]').val(svlelist_data[selected_id].vle_url);
            $('input[name=vle_username]').focus();
        } else {
            $('input[name=vle_name]').val("");
            $('input[name=vle_type]').val("");
            $('input[name=vle_url]').val("");
        }
    });


});