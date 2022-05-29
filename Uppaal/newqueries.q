//This file was generated from (Academic) UPPAAL 4.1.26 (rev. C3644FEF32EA19FE), December 2021

/*

*/
A[] (tempSensor.AirconditionLow or tempSensor.AirconditionHigh) imply ( aircondition.CoolingActive) and temp >19

/*

*/
(tempSensor.Warm && windowOpen==true) or (tempSensor.Hot && windowOpen==true) --> tempSensor.Fan

/*

*/
A[] (tempSensor.Fan) imply fan.Active and temp >19

/*

*/
A[] (tempSensor.Chilled or tempSensor.Cold) imply ( aircondition.HeatingActive) and temp < 31

/*

*/
E<> tempSensor.Chilled

/*

*/
E<> tempSensor.Warm

/*

*/
sup{tempSensor.AirconditionLow} : BTT

/*

*/
inf{tempSensor.AirconditionLow} : BTT

/*

*/
sup{tempSensor.Chilled} : tempSensor.coldTime

/*

*/
inf{tempSensor.Chilled} : tempSensor.coldTime

/*

*/
A[] deadlock imply (device2error == true or device1error == true)

/*

*/
A[] not deadlock imply (device2error == false and device1error == false)

/*

*/
humiditysensor.Dihumidify imply (dihumidifier.Started && humidity > 60)

/*

*/
humiditysensor.NeedHumidity imply (humidifier.Started && humidity < 40)

/*

*/
A[] humiditysensor.Stop imply (dihumidifier.Stopped && humidifier.Stopped && humidity < 60 && humidity > 40)

/*

*/
(humiditysensor.NeedHumidity && humidity < 60 && humidity > 40) --> humiditysensor.Stop

/*

*/
(humiditysensor.Dihumidify && humidity < 60 && humidity > 40) --> humiditysensor.Stop

/*

*/
E<> openPercentage == 100

/*

*/
E<> windowOpener.Idle

/*

*/
E<> openPercentage == 20

/*

*/
A<> co2Sensor.NeedO2 imply co2Sensor.Optimal

/*

*/
A<> co2Sensor.NeedO2 imply windowOpener.Opening

/*

*/
A<> co2 < 2100

/*

*/
A[] (co2Sensor.NeedO2 and co2 > 1000) imply windowOpener.Open

/*

*/
A[] co2Sensor.NeedO2 imply CO2HealthTimer <= 60000

/*

*/
sup{co2Sensor.NeedO2}:CO2HealthTimer

/*

*/
A[] moisture.NotNeedWater imply MHealthTimer >= 5000

/*

*/
A[] (moisture.NeedWater && device1error == false) imply MHealthTimer <=153

/*

*/
A[] device1.Unhealthy imply pump.SafeState

/*

*/
A<> (moisture.NeedWater and moist <100) imply pump.Active

/*

*/
A[] (moisture.NotNeedWater and moist >150) imply pump.NotActive

/*

*/
A[] moist<200

/*

*/
A[] moist < 60 imply device1error == true

/*
Not working
*/
A<> pump.Active imply moisture.NeedWater

/*

*/
sup{moisture.NeedWater and device1error == false } : MHealthTimer

/*

*/
sup{humiditysensor.NeedHumidity and device2error == false} : HHealthTimer
