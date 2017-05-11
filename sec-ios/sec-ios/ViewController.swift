import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        do {
            
            let publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDNGf2+RMon7e4sjWGEFpQt1FIOvL3OcsDC26S/mPEvvErv0HK1jIAcLuDRtFHoEqRpJg0skcf6LU8dxG3s7hg7W1c9Ayi6j3xv6UjOR6gCeu1DcfU9TIAXCpDWUSDOsRbS7sDcAbPy9U9kl00epbCGd5cGf/PmvlmjH2grm9QzQkYmvpFAzOW0jYJJkDW85b2aC1Da6hX1PUHBQ8dcGw8WMmsI7PlI4wZlgnzYorQ08fjHFbRPaWCK5ros5kbNKtIbZPwC3gEl3hzeiY7MotgedLTFXFCCuDu1qSIFNCgnkG0MuL1qxheDncw6WzQMUcCrYy1wwFpvRoqfIlhvvqwIDAQAB"
        
            //Realizar chamada ao serviço de login para obter o token com a chave 3des
            //Procedimento evitado para simplificar, porém, similar ao que foi feito no Android
            let data = "sYpkzuKlMwFs4d9ccvZQJ+RlG+/3nKYIYJArC11Q6r8OeNIurwumw3gcFG7CkCZdH6QBZEWKatHyTitxeiw26HXRdZLK+J11+9LwfwLUsArywA4lxBBwCVt27LdJ10gpnm8q7f5BD3YL8NdXh9QIPEyOOSRzd2iAC2H9zHS9Hv7Lv+T+cI2y5EPSFUQRMwujc1RBK7bMRy5xQtf1zAi78hOAg0t+DuHMnhox1KuisYW+ZgSPAg+8zL0oyxjiw911cLjtY+gwegDgJfC0ccHG4k9GLJ7REtk8ecpCRPSvddDS4uuecoT4G65cfohgIm8TezFEsiyx/fQCiNvNz7lrAw=="
        
        
            let tripleDesSession = try RSAUtil.decryptString(data, publicKey: publicKey)
        
            let session: [String: String] = convertToDictionary(text: tripleDesSession!)!
        
            print(session)
        
        
            //A chave sera alterada para tripleDes onde hoje está triploDes
        
            let tripleDesKey = session["triploDesKey"]!
            
            print(tripleDesKey)
            
            let authTripleDes = ["timestamp": "2017-05-02 22:50:15", "ip": "1274.0.0.1", "platform": "IOS"]
        
            let jsonData = try JSONSerialization.data(withJSONObject: authTripleDes, options: [])
            
            let json = String(data: jsonData,encoding: .utf8)
        
            print(json)
            
            //Token que será enviado na autenticacao
            let token = try TripleDesUtil.tripleDesEncryptString(json, key: tripleDesKey)
            
            print(token.base64EncodedString())
            
            print(try TripleDesUtil.tripleDesDecryptData(token, key: tripleDesKey))
            
            
        } catch {
            print(error.localizedDescription)
        }
        
    }
    
    func convertToDictionary(text: String) -> [String: String]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: String]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    

    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

