/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package glueps.adaptors.ARbrowsers.service;

import glueps.adaptors.ARbrowsers.model.Poi;

/**
 *
 * This is a severe modification of ARTags code  [ARTags project owners (see http://www.artags.org), @author Pierre Levy]
 * @author Juan A. Muñoz, GSIC-EMIC research group (see www.gsic.uva.es)
 */
public class SortPoi implements Comparable
{
    Poi poi;
    double dist;

    SortPoi( Poi poi , double lat , double lon )
    {
        this.poi = poi;      
        dist = distFrom(poi.getLat(), poi.getLon(), lat, lon);
    }

    public int compareTo(Object t)
    {
        return (int) ( this.dist - ((SortPoi) t).dist );
    }
    
    
    // Java implementation of Haversine formula
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6378100;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
        }
    
    

}
