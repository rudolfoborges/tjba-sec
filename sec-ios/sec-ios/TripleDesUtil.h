#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonCryptor.h>

@interface TripleDesUtil : NSObject

+ (NSData *)tripleDesEncryptString:(NSString *)input key:(NSString *)key error:(NSError **)error;

+ (NSString *)tripleDesDecryptData:(NSData *)input key:(NSString *)key error:(NSError **)error;

@end
