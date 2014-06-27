<?php
// $Id: index.php,v 1.5 2007/04/30 17:08:54 skodak Exp $

/**
 * @file
 * Manage the list of authorized clients / consumers.
 *
 * Quick API: a RESTful web service for a variety of Moodle resources.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2008.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

define('QAPI_CLIENT_TAB', 'qapi_client'); // database table name

require_once('../../../config.php');    // Moodle convention, but hackish
require_once($CFG->libdir .'/adminlib.php');

require_login();
admin_externalpage_setup('reportqapi');  // fixme todo
require_capability('moodle/site:config', get_context_instance(CONTEXT_SYSTEM));  // what's this do?  todo

admin_externalpage_print_header();          // page struct w/admin menu
print_heading('Allowed client list');    // title

// Step must be one of:
// input    insert a new client host IP and matching shared key for encryption
// delete   delete the matching record based upon primary key row "id"
$step   = optional_param('step', NULL, PARAM_ALPHA);

/// If data submitted, process and store
if (($form = data_submitted()) && confirm_sesskey()) {
    switch ($step) {
    case 'input':
        if (!empty($form->ipaddr) && !empty($form->sharedkey)) {
            $form->ipaddr = clean_param($form->ipaddr, PARAM_HOST);
            $form->sharedkey = clean_param($form->sharedkey, PARAM_ALPHANUM);   // todo
            // prevent duplicate records:
            if (count_records(QAPI_CLIENT_TAB, 'clientip', $form->ipaddr) > 0) {
                // found at least one record with same client IP address
                print_box('Error: duplicate client IP address');
            } else {
                // insert the new record
                $data = new stdClass;
                $data->clientip = $form->ipaddr;
                $data->sharedkey = $form->sharedkey;
                if (insert_record(QAPI_CLIENT_TAB, $data, FALSE)) {
                    print_box("New client {$form->ipaddr} added");
                } else {
                    print_box("Unable to add new client", 'errorbox');
                }
            }
        } else {
            print_box('Missing parameter', 'errorbox');
        }
        break;

    case 'delete':
        if (!empty($form->id)) {
            $form->id = clean_param($form->id, PARAM_INT);
            if (delete_records(QAPI_CLIENT_TAB, 'id', $form->id)) {
                print_box("Client {$form->id} deleted");
            } else {
                print_box("Unable to delete record id '{$form->id}'", 'errorbox');
            }
        } else {
            print_box('Missing ID', 'errorbox');
        }

        break;

    default:
        // unrecognized, invalid, possibly dangerous input values
        print_box('Invalid input', 'errorbox');
    }

}

// Display current list of hosts and editing form.
?>

<div id="trustedhosts"><!-- See theme/standard/styles_layout.css #trustedhosts .generaltable for rules -->

<table width='100%' cellpadding="5" class="generaltable generalbox standard" >
    <tr>
        <th class="header c0"><?php echo 'IP address'; ?></th>
        <th class="header c1"><?php echo 'Shared key'; ?></th>
        <th class="header c2"></th>
    </tr>

    <?php
    foreach (qapi_get_allowed_clients() as $client) {
    ?>
    <tr>
        <td class="cell c1"> <?php echo $client->clientip; ?> </td>
        <td class="cell c2"> <?php  echo $client->sharedkey; ?> </td>
        <td class="cell c3">
            <form method="post" action="index.php">
                <input type="hidden" name="sesskey" value="<?php echo $USER->sesskey ?>" />
                <input type="hidden" name="id" value="<?php echo $client->id; ?>" />
                <input type="hidden" name="step" value="delete" />
                <input type="submit" name="submit" value="<?php print_string('delete'); ?>"/>
            </form>
        </td>
    </tr>
    <?php
    }   // end foreach ()
    ?>

</table>

<form method="post" action="index.php">
    <input type="hidden" name="sesskey" value="<?php echo $USER->sesskey ?>" />
    <input type="hidden" name="step" value="input" />
    <table cellpadding="5" class="generaltable generalbox standard" >
        <tr>
            <th class="header c2" colspan="2">
                Add new client
            </th>
        </tr>
        <tr>
            <td class="cell c0">IP address:</td>
            <td class="cell c1"><input type="text" name="ipaddr" value="" /></td>
        </tr>
        <tr>
            <td class="cell c0">Shared key:</td>
            <td class="cell c1"><input type="text" name="sharedkey" value="unused for now"  readonly="true" /></td>
        </tr>
        <tr>
            <td class="cell c2" align="right" colspan="2"><input type="submit" value="Add client" /></td>
        </tr>
    </table>
</form>

</div>

<?php
admin_externalpage_print_footer();

require_once($CFG->libdir .'/dmllib.php');
function qapi_get_allowed_clients() {
    global $CFG;                        // watch out!
    $clients = get_records_sql('SELECT id, clientip, sharedkey FROM '. $CFG->prefix .QAPI_CLIENT_TAB);
    if (empty($clients)) $clients = array();
    return $clients;
}
?>
