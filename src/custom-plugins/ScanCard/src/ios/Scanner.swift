import UIKit
import AVFoundation
import Vision

class Scanner: UIViewController, CreditCardScannerViewControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()

        let creditCardScannerViewController = CreditCardScannerViewController(delegate: self)
        present(creditCardScannerViewController, animated: true)
    }

    func creditCardScannerViewController(_ viewController: CreditCardScannerViewController, didErrorWith error: CreditCardScannerError) {
        viewController.dismiss(animated: true)
        print(error.errorDescription ?? "Unknown error")
    }

    func creditCardScannerViewController(_ viewController: CreditCardScannerViewController, didFinishWith card: CreditCard) {
        // Do something with credit card info
        print("\(card)")
    }

}
}