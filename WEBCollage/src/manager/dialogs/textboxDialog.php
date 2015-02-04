<table>
    <tr>
        <td>
            <?php 
				$id = $_GET['id'];
            	echo "<input id=\"$id\" dojoType=\"dijit.form.TextBox\" name=\"value\">";
			?>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <button dojoType="dijit.form.Button" label="Aceptar" type="submit">
            </button>
        </td>
    </tr>
</table>
