/**
 * Some functions related to the calculation of distances
 */

/**
 * Convert to radians a value
 * @param number the value to convert
 * @return the result of the conversion
 */
function toRadians(number) {
	   return number * Math.PI / 180;
}

/**
 * Javascript implementation of Haversine formula
 * @param lat1 latitude of the first point
 * @param lng1 longitude of the first point
 * @param lat2 latitude of the second point
 * @param lng2 longitude of the second point
 * @return the distance between both points
 */
function distFrom(lat1, lng1, lat2, lng2) {
    var earthRadius = 6378100;
    var dLat = toRadians(lat2-lat1);
    var dLng = toRadians(lng2-lng1);
    var sindLat = Math.sin(dLat / 2);
    var sindLng = Math.sin(dLng / 2);
    var a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
            * Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2));
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var dist = earthRadius * c;

    return dist;
}