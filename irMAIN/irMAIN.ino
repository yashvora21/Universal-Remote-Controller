#include <SoftwareSerial.h>
#include <IRremote.h>

SoftwareSerial mySerial(0, 1); // RX, TX

int RECV_PIN = 11;
int STATUS_PIN = 13;
IRsend irsend;

IRrecv irrecv(RECV_PIN);
decode_results results;

void setup()  
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  
  pinMode(STATUS_PIN, OUTPUT);
  pinMode(8, OUTPUT);
  digitalWrite(8, HIGH);
  while (!Serial) {
Serial.println("Connected");
    ; // wait for serial port to connect. Needed for Leonardo only
  }


  Serial.println("Goodnight moon!");

  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  mySerial.println("setup");
}

// Storage for the recorded code
int codeType = -1; // The type of code
unsigned long codeValue; // The code value if not raw
unsigned int rawCodes[RAWBUF]; // The durations if raw
int codeLen; // The length of the code
int toggle = 0; // The RC5/6 toggle state


void storeCode(decode_results *results) {
  codeType = results->decode_type;
  int count = results->rawlen;
  if (codeType == UNKNOWN)
  {
    Serial.println("Received unknown code, saving as raw");
  }
  else if (codeType == NEC) 
  {
      Serial.print("Received NEC: ");
      if (results->value == REPEAT) 
      {
        // Don't record a NEC repeat value as that's useless.
        Serial.println("repeat; ignoring.");
        return;
      }
    } 
    else if (codeType == SONY) 
    {
      Serial.print("Received SONY: ");
    } 
    else if (codeType == RC5) 
    {
      Serial.print("Received RC5: ");
    } 
    else if (codeType == RC6) 
    {
      Serial.print("Received RC6: ");
    } 
    else 
    {
      Serial.print("Unexpected codeType ");
      Serial.print(codeType, DEC);
      Serial.println("");
    }
    Serial.println(results->value, HEX);
    codeValue = results->value;
    codeLen = results->bits;
  }






void sendCode() {
  if (codeType == NEC) {
      irsend.sendNEC(codeValue, codeLen);
      Serial.print("Sent NEC ");
      Serial.println(codeValue, HEX);
  } 
  else if (codeType == SONY) {
    irsend.sendSony(codeValue, codeLen);
    Serial.print("Sent Sony ");
    Serial.println(codeValue, HEX);
  } 
  else if (codeType == RC5 || codeType == RC6) {
    // Put the toggle bit into the code to send
    codeValue = codeValue & ~(1 << (codeLen - 1));
    codeValue = codeValue | (toggle << (codeLen - 1));
    if (codeType == RC5) {
      Serial.print("Sent RC5 ");
      Serial.println(codeValue, HEX);
      irsend.sendRC5(codeValue, codeLen);
    } 
    else {
      irsend.sendRC6(codeValue, codeLen);
      Serial.print("Sent RC6 ");
      Serial.println(codeValue, HEX);
    }
  } 
  else if (codeType == UNKNOWN /* i.e. raw */) {
    // Assume 38 KHz
    irsend.sendRaw(rawCodes, codeLen, 38);
    Serial.println("Sent raw");
  }
}









int appstatus;
void loop() // run over and over
{
  if (mySerial.available())
  {
    appstatus=mySerial.read();
    Serial.println("appstatus :"+appstatus);
    if(appstatus==1)  //========================(1)IR recieving Mode
    {
      
      Serial.println("IR Recieving mode");
      int res[5];//=new decode_results[5];
      int cnt=1;
      irrecv.enableIRIn();
      while (!irrecv.decode(&results));
  /*    res[0]=results.value;
      while(cnt<5)
      {
        Serial.println(results.value);
        Serial.println(cnt);
      irrecv.enableIRIn();
      while (!irrecv.decode(&results));
      int j=0;
      for(j=0;j<cnt;j++)
      if(res[j]!=results.value)
        {
          Serial.print(res[j]);
          Serial.print("-");
          Serial.print(j);
          Serial.print("!=");
          Serial.println(results.value);
          
        break;
      }
      if(j==cnt)
      {
        res[cnt]=results.value;
                cnt++;
      }
      else
      {
        cnt=1;
        res[0]=results.value;
      }
      }  
*/      
          digitalWrite(STATUS_PIN, HIGH);
          storeCode(&results);
          irrecv.resume(); // resume receiver
          mySerial.print("|"); // ======================(|)value
                Serial.println("|");
          mySerial.print(results.value);
          Serial.println(results.value);
          mySerial.print("|"); // ======================(|)bits
           Serial.println("|");
          mySerial.print(results.bits);
           Serial.println(results.bits);
          mySerial.print("|"); // ======================(|)type
           Serial.println("|");
           
          mySerial.print(results.decode_type);
          Serial.println(results.decode_type);
                    mySerial.print("|"); 
           Serial.println("|");
           mySerial.flush();
           Serial.print("f");
          digitalWrite(STATUS_PIN, LOW);
        
    }
    if(appstatus==0)  // ======================(0)IR trasmitting Mode
    {
            Serial.println("IR transmitting mode");
      int d;
      int cnt=0,rep=0;
      codeValue=0;
      codeLen=0;
      codeType=0;     
      while(true)
      {
        d=mySerial.read();
	Serial.println(d);
        if(d=='|')
        {
          cnt++;
        }
      if(d>=0 && d<=9)
      {
        if(cnt==1)
        {
        codeValue=codeValue*10+d;
        Serial.println("codevalue :"+codeValue);
        }
        else if( cnt==2)
        {
        codeLen=codeLen*10+d;
        Serial.println("codelength :"+codeLen);
        }
        else if(cnt==3)
        {
        codeType=codeType*10+d;
        Serial.println("codetype :"+codeType);
        }
        else if(cnt==4)
        {
        rep=rep*10+d;
        Serial.println("no :"+rep);
        }
      }
      if(cnt==5)
        {
          break;
        }
      
      }
      Serial.print("codevalue :");
      Serial.println(codeValue);
      Serial.print("codeLen :");
      Serial.println(codeLen);
      Serial.print("codeType :");
      Serial.println(codeType);
      Serial.print("no :");
      Serial.println(rep);
      
      if(rep==0)
        sendCode();
        
      for(int i=0;i<rep;i++)
      {
        sendCode();
        delay(1000);
      }
    }
    if(appstatus==2)
    {
      Serial.println("Appstatus:2");
      digitalWrite(8, LOW);
      delay(200);
       digitalWrite(8, HIGH);      
    }
  }
}
