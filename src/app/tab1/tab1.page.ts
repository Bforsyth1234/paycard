import { Component, OnInit } from '@angular/core';
declare var device: any;
declare var ScanCard: any;

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page implements OnInit {

  constructor(private window: Window) {

  }
  ngOnInit() {
    document.addEventListener("deviceready", function () {
      console.log("Device is ready!");
      alert(device.platform);
    }, false);
  }


  scanCard() {


      console.log('RAN RAN RAN');
      ScanCard.launchScanCard('', (result: any) => {
        console.log('result = ');
        console.log(result);
      }, (err: any) => {
        console.log('err = ');
        console.log(err);
      });
    
  }

}
