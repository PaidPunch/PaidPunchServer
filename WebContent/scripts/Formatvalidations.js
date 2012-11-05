
// ------------------------------------------------------------------
// fnValidate ( DType, Value, FromRng, ToRng, Format )
// Validates the datatype first
// For the given datatype and value this function checks for the format and 
// the range given by the user
// This function flashes the necessary alerts
// ------------------------------------------------------------------

function fnValidate( DType, Value, FromRng, ToRng, Format )
{
  switch( DType )
  {
    case "Integer":
      if( fnCheckNumberFormat( Value, Format ) )
      {
        if( FromRng != 0 || ToRng != 0 )
        {
          fnChkNumberRange( Value, FromRng, ToRng ) ;
        }
      }
      break ;
    case "Decimal":
      if( fnCheckDecimal( Value, Format ) )
      {
        if( FromRng != 0 || ToRng != 0 )
        {
          fnChkNumberRange( Value, FromRng, ToRng ) ;
        }
      }
      break ;
    case "Date":
      if( fnisDate( Value, Format ) )
      {
        alert( "Entered date is valid" ) ;
      }
      else
      {
        alert( "Entered date is not a valid date..." ) ;
        break ;
      }

      if( FromRng != "" || ToRng != "" )
      {
        var isLessThnFromRng = fncompareDates( Value, Format, FromRng, Format )
        var isGreaterThnToRng = fncompareDates( Value, Format, ToRng, Format )
        if( isLessThnFromRng == 0 || isGreaterThnToRng == 1 )
        {
          alert( "Entered date is out of range" ) ;
        }
      }
      break ;
    case "Time":
        if( fnisDate( Value, Format ) )
        {
          alert( "Entered Time is valid" ) ;
        }
        else
        {
          alert( "Entered Time is not a valid date..." ) ;
          break ;
        }
      break ;
    default:
      alert( "Specified data type is not valid " ) ;
      break ;
  }
}

// ------------------------------------------------------------------
// fnChkNumberRange( Value, FromRng, ToRng )
// Checks for the value falls within the given range
// flashes the appropriate alert
// ------------------------------------------------------------------

function fnChkNumberRange( Value, FromRng, ToRng )
{
  if( Value < FromRng || Value > ToRng )
  {
    alert( "Entered value is out of range" ) ;
  }
}

var MONTH_NAMES=new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec') ;
var DAY_NAMES=new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
function LZ(x) {return(x<0||x>9?"":"0")+x}

// ------------------------------------------------------------------
// fnisDate ( date_string, format_string )
// Returns true if date string matches format of format string and
// is a valid date. Else returns false.
// It is recommended that you trim whitespace around the value before
// passing it to this function, as whitespace is NOT ignored!
// ------------------------------------------------------------------
function fnisDate(val,format)
{
    var date=fngetDateFromFormat(val,format);
    if (date==0) { return false; }
      return true;
}
// -------------------------------------------------------------------
// fncompareDates(date1,date1format,date2,date2format)
//   Compare two date strings to see which is greater.
//   Returns:
//   1 if date1 is greater than date2
//   0 if date2 is greater than date1 of if they are the same
//  -1 if either of the dates is in an invalid format
// -------------------------------------------------------------------
function fncompareDates(date1,dateformat1,date2,dateformat2)
{
   var d1=fngetDateFromFormat(date1,dateformat1);
   var d2=fngetDateFromFormat(date2,dateformat2);
   if (d1==0 || d2==0) {
      return -1;
   }
   else if (d1 > d2) {
      return 1;
   }
   return 0;
}

// ------------------------------------------------------------------
// fnformatDate (date_object, format)
// Returns a date in the output format specified.
// The format string uses the same abbreviations as in fngetDateFromFormat()
// ------------------------------------------------------------------
function fnformatDate(date,format) {
        format=format+"";
        var result="";
        var i_format=0;
        var c="";
        var token="";
        var y=date.getYear()+"";
        var M=date.getMonth()+1;
        var d=date.getDate();
        var E=date.getDay();
        var H=date.getHours();
        var m=date.getMinutes();
        var s=date.getSeconds();
        var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
        // Convert real date parts into formatted versions
        var value=new Object();
        if (y.length < 4) {y=""+(y-0+1900);}
        value["y"]=""+y;
        value["yyyy"]=y;
        value["yy"]=y.substring(2,4);
        value["M"]=M;
        value["MM"]=LZ(M);
        value["MMM"]=MONTH_NAMES[M-1];
        value["NNN"]=MONTH_NAMES[M+11];
        value["d"]=d;
        value["dd"]=LZ(d);
        value["E"]=DAY_NAMES[E+7];
        value["EE"]=DAY_NAMES[E];
        value["H"]=H;
        value["HH"]=LZ(H);
        if (H==0){value["h"]=12;}
        else if (H>12){value["h"]=H-12;}
        else {value["h"]=H;}
        value["hh"]=LZ(value["h"]);
        if (H>11){value["K"]=H-12;} else {value["K"]=H;}
        value["k"]=H+1;
        value["KK"]=LZ(value["K"]);
        value["kk"]=LZ(value["k"]);
        if (H > 11) { value["a"]="PM"; }
        else { value["a"]="AM"; }
        value["m"]=m;
        value["mm"]=LZ(m);
        value["s"]=s;
        value["ss"]=LZ(s);
        while (i_format < format.length) {
                c=format.charAt(i_format);
                token="";
                while ((format.charAt(i_format)==c) && (i_format < format.length)) {
                        token += format.charAt(i_format++);
                        }
                if (value[token] != null) { result=result + value[token]; }
                else { result=result + token; }
                }
        return result;
        }

// ------------------------------------------------------------------
// Utility functions for parsing in fngetDateFromFormat()
// ------------------------------------------------------------------
function _isInteger(val) {
        var digits="1234567890";
        for (var i=0; i < val.length; i++) {
                if (digits.indexOf(val.charAt(i))==-1) { return false; }
                }
        return true;
        }
function _getInt(str,i,minlength,maxlength) {
        for (var x=maxlength; x>=minlength; x--) {
                var token=str.substring(i,i+x);
                if (token.length < minlength) { return null; }
                if (_isInteger(token)) { return token; }
                }
        return null;
        }

// ------------------------------------------------------------------
// fngetDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
function fngetDateFromFormat(val,format) {
        val=val+"";
        format=format+"";
        var i_val=0;
        var i_format=0;
        var c="";
        var token="";
        var token2="";
        var x,y;
        var now=new Date();
        var year=now.getYear();
        var month=now.getMonth()+1;
        var date=1;
        var hh=now.getHours();
        var mm=now.getMinutes();
        var ss=now.getSeconds();
        var ampm="";

        while (i_format < format.length) {
                // Get next token from format string
                c=format.charAt(i_format);
                token="";
                while ((format.charAt(i_format)==c) && (i_format < format.length)) {
                        token += format.charAt(i_format++);
                        }
                // Extract contents of value based on format token
                if (token=="yyyy" || token=="yy" || token=="y") {
                        if (token=="yyyy") { x=4;y=4; }
                        if (token=="yy")   { x=2;y=2; }
                        if (token=="y")    { x=2;y=4; }
                        year=_getInt(val,i_val,x,y);
                        if (year==null) { return 0; }
                        i_val += year.length;
                        if (year.length==2) {
                                if (year > 70) { year=1900+(year-0); }
                                else { year=2000+(year-0); }
                                }
                        }
                else if (token=="MMM"||token=="NNN"){
                        month=0;
                        for (var i=0; i<MONTH_NAMES.length; i++) {
                                var month_name=MONTH_NAMES[i];
                                if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
                                        if (token=="MMM"||(token=="NNN"&&i>11)) {
                                                month=i+1;
                                                if (month>12) { month -= 12; }
                                                i_val += month_name.length;
                                                break;
                                                }
                                        }
                                }
                        if ((month < 1)||(month>12)){return 0;}
                        }
                else if (token=="EE"||token=="E"){
                        for (var i=0; i<DAY_NAMES.length; i++) {
                                var day_name=DAY_NAMES[i];
                                if (val.substring(i_val,i_val+day_name.length).toLowerCase()==day_name.toLowerCase()) {
                                        i_val += day_name.length;
                                        break;
                                        }
                                }
                        }
                else if (token=="MM"||token=="M") {
                        month=_getInt(val,i_val,token.length,2);
                        if(month==null||(month<1)||(month>12)){return 0;}
                        i_val+=month.length;}
                else if (token=="dd"||token=="d") {
                        date=_getInt(val,i_val,token.length,2);
                        if(date==null||(date<1)||(date>31)){return 0;}
                        i_val+=date.length;}
                else if (token=="hh"||token=="h") {
                        hh=_getInt(val,i_val,token.length,2);
                        if(hh==null||(hh<1)||(hh>12)){return 0;}
                        i_val+=hh.length;}
                else if (token=="HH"||token=="H") {
                        hh=_getInt(val,i_val,token.length,2);
                        if(hh==null||(hh<0)||(hh>23)){return 0;}
                        i_val+=hh.length;}
                else if (token=="KK"||token=="K") {
                        hh=_getInt(val,i_val,token.length,2);
                        if(hh==null||(hh<0)||(hh>11)){return 0;}
                        i_val+=hh.length;}
                else if (token=="kk"||token=="k") {
                        hh=_getInt(val,i_val,token.length,2);
                        if(hh==null||(hh<1)||(hh>24)){return 0;}
                        i_val+=hh.length;hh--;}
                else if (token=="mm"||token=="m") {
                        mm=_getInt(val,i_val,token.length,2);
                        if(mm==null||(mm<0)||(mm>59)){return 0;}
                        i_val+=mm.length;}
                else if (token=="ss"||token=="s") {
                        ss=_getInt(val,i_val,token.length,2);
                        if(ss==null||(ss<0)||(ss>59)){return 0;}
                        i_val+=ss.length;}
                else if (token=="a") {
                        if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
                        else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
                        else {return 0;}
                        i_val+=2;}
                else {
                        if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
                        else {i_val+=token.length;}
                        }
                }
        // If there are any trailing characters left in the value, it doesn't match
        if (i_val != val.length) { return 0; }
        // Is date valid for month?
        if (month==2) {
                // Check for leap year
                if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
                        if (date > 29){ return 0; }
                        }
                else { if (date > 28) { return 0; } }
                }
        if ((month==4)||(month==6)||(month==9)||(month==11)) {
                if (date > 30) { return 0; }
                }
        // Correct hours value
        if (hh<12 && ampm=="PM") { hh=hh-0+12; }
        else if (hh>11 && ampm=="AM") { hh-=12; }
        var newdate=new Date(year,month-1,date,hh,mm,ss);
        return newdate.getTime();
		//return newdate ;
        }

// ------------------------------------------------------------------
// fnCheckDecimal( Value, Format )
// Returns true if the decimal value matches the selected format
// else returns false and also flashes a alert
// ------------------------------------------------------------------

function fnCheckDecimal( Value, Format )
{
  var Pos = Format.indexOf( ".") ;
  var decimalRE ;

  //switch( Format.substring( Pos + 1 ).length )
  switch( Format )
  {
    // Cases for Upto 2 decimal digits
    case "1234.10":
      decimalRE = /^\d+\.\d{2}$/
      break ;

    case "-1234.10":
      decimalRE = /^-\d+\.\d{2}$/
      break ;

    case "1,234.10":
      decimalRE = /^\d{1,3}(,\d{3})+\.\d{2}$/
      break ;

    case "-1,234.10":
      decimalRE = /^-\d{1,3}(,\d{3})+\.\d{2}$/
      break ;

    // Cases for Upto 3 decimal digits
    case "1234.210":
      decimalRE = /^\d+\.\d{3}$/
      break ;

    case "-1234.210":
      decimalRE = /^-\d+\.\d{3}$/
      break ;

    case "1,234.210":
      decimalRE = /^\d{1,3}(,\d{3})+\.\d{3}$/
      break ;

    case "-1,234.210":
      decimalRE = /^-\d{1,3}(,\d{3})+\.\d{3}$/
      break ;

    // Cases for Upto 4 decimal digits
    case "1234.3210":
      decimalRE = /^\d+\.\d{4}$/
      break ;

    case "-1234.3210":
      decimalRE = /^-\d+\.\d{4}$/
      break ;

    case "1,234.3210":
      decimalRE = /^\d{1,3}(,\d{3})+\.\d{4}$/
      break ;

    case "-1,234.3210":
      decimalRE = /^-\d{1,3}(,\d{3})+\.\d{4}$/
      break ;

    default:
      decimalRE = /^\d+\.\d+$/
      break ;
  }

  if( Value.match( decimalRE ) == null )
  {
    alert( "Not a decimal number..." ) ;
    return false ;
  }
  else
  {
    alert( "Valid decimal number..." ) ;
    return true ;
  }
}

// ------------------------------------------------------------------
// fnCheckNumberFormat( Value, Format )
// Returns true if the number value matches the selected format
// else returns false and also flashes a alert
// ------------------------------------------------------------------

function fnCheckNumberFormat( Value, Format )
{
  var NumberRE ;

  switch( Format )
  {
    case "1,234":
      NumberRE = /^\d{1,3}(,\d{3})+$/g
      break ;
    case "1234":
      NumberRE = /^\d+$/g
      break ;
    case "-1234":
      NumberRE = /^-\d+$/g
      break ;
    case "-1,234":
      NumberRE = /^-\d{1,3}(,\d{3})+$/g
      break ;
    default:
      NumberRE = /^\d+$/g
      break ;
   }

  if( Value.match( NumberRE ) == null )
  {
    alert( "Not a Valid intger number..." ) ;
    return false ;
  }
  else
  {
    alert( "Valid Integer number..." ) ;
    return true ;
  }
}
///////////////////////////////////////
//This function Check user entered date format
//and change date format to user's date format
//////////////////////////////////////

function SetDtFormat( formName , ctrlName )
	{
		
	var szJSPName= formName ;
    var INPUTs = window.document.getElementsByTagName("input");
    var iTotalINPUTCount = INPUTs.length ;
	var count = 0;

	for( var iINPUTCount = 0 ; iINPUTCount < iTotalINPUTCount ; iINPUTCount++)
    {
		var currINPUT = INPUTs.item( iINPUTCount);
		if (currINPUT.getAttribute("name") == ctrlName)
		{
			count = count + 1;
		}
    }
  
	for(var i = 0 ; i < count ; i++)
	{
			if( count > 1 )
			{
			if(document.forms[ 0 ].elements[ ctrlName ] [ i ].value != "")
				{
					var JulianDt="" ;
					JulianDt = fngetDateFromFormat(document.forms[ 0 ].elements[ ctrlName ][ i ]  .value , "yyyy-MM-dd") ;
					var ReturnedDt = new Date(JulianDt) ;
					var DtStr = fnformatDate( ReturnedDt, "dd/MM/yyyy" ) ; // Ventage's date format
					document.forms[ 0 ].elements[ ctrlName ][ i ].value  = DtStr ;
				}
			}
			else
			{
			if(document.forms[ 0 ].elements[ ctrlName ].value != "")
				{
					var JulianDt="" ;
					JulianDt = fngetDateFromFormat( document.forms[ 0 ].elements[ ctrlName ].value , "yyyy-MM-dd") ;
					var ReturnedDt = new Date(JulianDt) ;
					var DtStr = fnformatDate( ReturnedDt, "dd/MM/yyyy" ) ; // CE's date format
					document.forms[ 0 ].elements[ ctrlName ].value = DtStr ;
				}
			}
	}//for
}
//////////////
///////////////////////////////////////
//This function Check user entered date format
//and change date format to database date format
//////////////////////////////////////
function ChangeDtFormat( formName , ctrlName )
	{			
	var szJSPName= formName ;
    var INPUTs = window.document.getElementsByTagName("input");
    var iTotalINPUTCount = INPUTs.length ;
	var count = 0;

	for( var iINPUTCount = 0 ; iINPUTCount < iTotalINPUTCount ; iINPUTCount++)
    {
		var currINPUT = INPUTs.item( iINPUTCount);
		if (currINPUT.getAttribute("name") == ctrlName)
		{
			count = count + 1;
		}
    }
	for(var i = 0 ; i < count ; i++)
	{	
		if( count > 1 )
			{
				
			if(document.forms[ 0 ].elements[ ctrlName ][ i ].value != "")
				{
					
					var JulianDt="" ;
					JulianDt = fngetDateFromFormat(document.forms[ 0 ].elements[ ctrlName ][ i ] .value , "dd/MM/yyyy") ;
					if ( JulianDt == 0 )
					{
              //         alert("Date is not in dd/mm/yyyy "); 
					   document.forms[ 0 ].elements[ ctrlName ][ i ].focus();
					   return false;
					}
					else
                    {
					var ReturnedDt = new Date(JulianDt) ;
					var DtStr = fnformatDate( ReturnedDt, "yyyy-MM-dd" ) ; // Ventage's date format yyyy-MM-dd
					document.forms[ 0 ].elements[ ctrlName ][ i ].value  = DtStr ;
					}
				}
			}
			else
			{
			if(document.forms[ 0 ].elements[ ctrlName ].value != "")
				{
					var JulianDt="" ;
					JulianDt = fngetDateFromFormat( document.forms[ 0 ].elements[ ctrlName ].value , "dd/MM/yyyy") ;
					if(JulianDt == 0)
					{
                   //    alert("Date is not in dd/mm/yyyy "); 
					   document.forms[ 0 ].elements[ ctrlName ].focus();
					   return false;
					}
					else
					{
					var ReturnedDt = new Date(JulianDt) ;
					var DtStr = fnformatDate( ReturnedDt, "yyyy-MM-dd" ) ; // CE's date format
					document.forms[ 0 ].elements[ ctrlName ].value = DtStr ;
					}
				}
			}
	}//for
}