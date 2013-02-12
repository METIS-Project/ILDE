<?php

	/**
	 * Elgg Autocomplete
	 * 
	 * @package autocomplete
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Pedro Prez
	 * @copyright 2009
	 * @link http://www.pedroprez.com.ar/
	 * @link2 http://www.keetup.com/pedroprez/
 	*/
	
	function autocomplete_init()
	{
		//** VIEWS	
		// Extend CSS
			extend_view('css','autocomplete/css');
	}
	
	//**BEGIN
	register_elgg_event_handler('init','system','autocomplete_init');
?>
