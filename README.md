DisplayDirect
=============

DisplayDirect is een project van CROW-NDOV om de reisinformatieketen voor Dynamische ReizigersInformatieSystemen (DRIS) te vereenvoudigen en te centraliseren. Waar voorheen in een klassiek DRIS project een deelketen moest worden opgezet voor de distributie- en beheerfuncties voor displays, is DisplayDirect een centrale distributiefunctie die voor alle displays in Nederland in te zetten is. 


Modules
--------
**Applicaties**
- *distribution* - distributiefunctie van het DisplayDirect project. Verwerkt KV78Turbo en zet dit om naar berichten op een MQTT broker
- *dashboard* - een eenvoudige versie van een dashboard voor het verwerken en tonen van statusinformatie en logging
- *virtual_screen* - een voorbeeld haltefunctie die via de browser te bedienen is

**Libraries**
- *common* - een library met generieke componenten die in elke DisplayDirect applicatie relevant zijn. Dit is onder andere de code voor protobuf berichten maar bijvoorbeeld ook code voor het bepalen van topics.
- *common_web* - generieke hulpmiddelen voor het maken van een web-applicatie (alle applicaties hierboven genoemd hebben een web-frontend)
- *common_client* - een generieke basis voor haltesysteem waarbij implementatie-specifieke zaken zo veel mogelijk als interfaces zijn gedefinieerd.

Ontwikkelen
----------
Dit project is een Gradle project, door middel van de bijgeleverde Gradle Wrapper kan dit project worden gebouwd, of met de ontwikkelomgeving van je keuze. 
De applicaties zijn ontwikkel om te draaien als Tomcat applicaties onder Java 8 (of hoger).
Alle applicaties gaan er van uit dat er lokaal een MQTT broker draait voor ontwikkeldoeleinden. Dit kan in de configuratie bestanden (```resource/configuration.properties```) worden aangepast. 

Installeren
----------
De handleiding voor het installeren van virtuele halte, dashboard of distributiefunctie volgt.