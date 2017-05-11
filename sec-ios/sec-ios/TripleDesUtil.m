
#import "TripleDesUtil.h"
#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonCryptor.h> 

@implementation TripleDesUtil


+ (NSData *)tripleDesEncryptString:(NSString *)input
                               key:(NSString *)key
                             error:(NSError **)error
{
    NSParameterAssert(input);
    NSParameterAssert(key);
    
    NSData *inputData = [input dataUsingEncoding:NSUTF8StringEncoding];
    //NSData *keyData = [key dataUsingEncoding:NSUTF8StringEncoding];
    NSData *keyData = [[NSData alloc] initWithBase64EncodedString:key options:0];
    
    size_t outLength;
    
    NSMutableData *outputData = [NSMutableData dataWithLength:(inputData.length  +  kCCAlgorithm3DES)];
    
    CCCryptorStatus
    result = CCCrypt(kCCEncrypt, // operation
                     kCCAlgorithm3DES, // Algorithm
                     kCCOptionECBMode | kCCOptionPKCS7Padding, // options
                     keyData.bytes, // key
                     keyData.length, // keylength
                     nil,// iv
                     inputData.bytes, // dataIn
                     inputData.length, // dataInLength,
                     outputData.mutableBytes, // dataOut
                     outputData.length, // dataOutAvailable
                     &outLength); // dataOutMoved
    
    if (result != kCCSuccess) {
        if (error != NULL) {
            *error = [NSError errorWithDomain:@"com.your_domain.your_project_name.your_class_name."
                                         code:result
                                     userInfo:nil];
        }
        return nil;
    }
    [outputData setLength:outLength];
    
    NSString *base64String = [outputData base64EncodedStringWithOptions:0];
    
    return outputData;
}


+ (NSString *)tripleDesDecryptData:(NSData *)input
                               key:(NSString *)key
                             error:(NSError **)error
{
    NSParameterAssert(input);
    NSParameterAssert(key);
    
    NSData *inputData = input;
    NSData *keyData = [[NSData alloc] initWithBase64EncodedString:key options:0];
    
    size_t outLength;
    
    NSAssert(keyData.length == kCCKeySize3DES, @"the keyData is an invalid size");
    
    NSMutableData *outputData = [NSMutableData dataWithLength:(inputData.length  +  kCCBlockSize3DES)];
    
    CCCryptorStatus
    result = CCCrypt(kCCDecrypt, // operation
                     kCCAlgorithm3DES, // Algorithm
                     kCCOptionPKCS7Padding | kCCOptionECBMode, // options
                     keyData.bytes, // key
                     keyData.length, // keylength
                     nil,// iv
                     inputData.bytes, // dataIn
                     inputData.length, // dataInLength,
                     outputData.mutableBytes, // dataOut
                     outputData.length, // dataOutAvailable
                     &outLength); // dataOutMoved
    
    if (result != kCCSuccess) {
        if (error != NULL) {
            *error = [NSError errorWithDomain:@"com.your_domain.your_project_name.your_class_name."
                                         code:result
                                     userInfo:nil];
        }
        return nil;
    }
    [outputData setLength:outLength];
    return [[NSString alloc] initWithData:outputData encoding:NSUTF8StringEncoding];
}


@end
