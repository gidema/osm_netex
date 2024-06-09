-- Delete ignorable user stop code from the CHB passenger_stop_assignment table
DELETE FROM chb.chb_psa
WHERE user_stop_code IN (
	SELECT user_stop_code
	FROM netex.ref_netex_ignore_user_stop_code
)
