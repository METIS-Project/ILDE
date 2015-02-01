
package glueps.adaptors.ARbrowsers.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


import java.net.URL;



public class Poi implements Serializable
{

    private String id;
    private String name;
    private double lat;
    private double lon;
    private Long date;
    private URL location;
    private int maxdistance;
    private String poitype;
    private String positionType;
    private String cosid;
    private String scale;
    private String description;
    private String orientation;
    private URL icon;  // not used currently
    



	public Poi()
    {
    }

    public Poi(String name, double lat, double lon)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.date = new Date().getTime();
    }
  
    public Poi(String name, String id, String description, URL location, double lat, double lon, int maxdistance, String poitype, String positionType, String scale, String orientation)
    {
        this.name = name;
        this.id = id;
        this.description = description;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.date = new Date().getTime();
        this.maxdistance = maxdistance;
        this.poitype = poitype;
        this.positionType = positionType;
        this.scale = scale;
        this.orientation = orientation;
    }
    public Poi(String name, String id, String description, URL location, String cosid, String poitype, String positionType, String scale, String orientation)
    {
        this.name = name;
        this.id = id;
        this.description = description;
        this.location = location;
        this.date = new Date().getTime();
        this.cosid = cosid;
        this.poitype = poitype;
        this.positionType = positionType;
        this.scale = scale;
        this.orientation = orientation;
    }
    
    public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}


    public URL getLocation() {
		return location;
	}

	public void setLocation(URL location) {
		this.location = location;
	}

	public void setPoiId(String id) {
		this.id = id;
	}
	public String getPoiId() {
		return id;
	}


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public double getLat()
    {
        return this.lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return this.lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }


    public long getDate()
    {
        return date;
    }


   public void setDate(long date)
    {
        this.date = date;
    }

   public URL getIcon() {
		return icon;
	}

	public void setIcon(URL icon) {
		this.icon = icon;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getCosid() {
		return cosid;
	}

	public void setCosid(String cosid) {
		this.cosid = cosid;
	}

	public String getPoitype() {
		return poitype;
	}

	public void setPoitype(String poitype) {
		this.poitype = poitype;
	}

	public int getMaxdistance() {
		return maxdistance;
	}

	public void setMaxdistance(int maxdistance) {
		this.maxdistance = maxdistance;
	}
	
   
   //Thanks to ARTags (see http://www.artags.org):
    public String getFormatedDate(Locale locale)
    {
    	String result = null;
        try {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
            String d = format.format(new Date(date)); 
			result = new String( d.getBytes( "UTF-8" ) );			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }






}
