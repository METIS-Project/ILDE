<?php

$mysql_version = 56;


class LdsQuery56
{

    public function __construct()
    {

    }

    public function all_can_view($acv_k, $acv_v) {
        $query['join'] = "";
        $query['permission'] = <<<SQL
    OR
	(
		m.name_id = '{$acv_k}' AND m.value_id = '{$acv_v}'
	)
    OR
    (
        e.guid NOT IN (
            SELECT DISTINCT acvm.entity_guid FROM metadata acvm WHERE acvm.name_id = '{$acv_k}'
        )
        AND
        e.access_id < 3 AND e.access_id > 0
    )
SQL;

        return $query;
    }

    public function permission($acv, $myfirstlds, $user_id) {

    }
}