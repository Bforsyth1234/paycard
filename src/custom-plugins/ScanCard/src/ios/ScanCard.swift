
import UIKit
import AVFoundation
import Vision

@objc(ScanCard) class ScanCard : CDVPlugin {

    @objc(launchScanCard:)
    func launchScanCard(command: CDVInvokedUrlCommand) {

        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        print("SCANNER: Launch Works")

        let scanner = Scanner(resultsHandler: { paymentCardNumber in            
            self.dismiss(animated: true, completion: nil)
        })
        // self.present(paymentCardExtractionViewController, animated: true, completion: nil)

        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        
        scanner.modalPresentationStyle = .fullScreen
        scanner.modalTransitionStyle = .crossDissolve
        
        present(scanner, animated: true, completion: nil)

        // pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Scan May Work somehow :)")
        // self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
    }
    
}