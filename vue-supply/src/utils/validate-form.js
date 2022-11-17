/*eslint-disable*/
const pPattern = { pattern: /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{8,20})$/, message: '请输入符合字母、数字组成的8-20位密码' }, // 密码
      pPattern2 = { pattern: /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{4,20})$/, message: '请输入符合字母、数字组成的4-20位密码' }, // 密码
      pPattern3 = { pattern: /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,20})$/, message: '请输入符合字母、数字组成的6-20位密码' }, // 密码
      isAccount = { pattern: /^([a-zA-Z][a-zA-Z0-9_]{3,19})$/, message: '请输入以英文字母开头的4-20个字母或数字' }, // 密码
      isTwoFloat = { pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/, message: '最多输入两位小数' },
      posPattern = { pattern: /^\d+$/, message: '请输入正整数' }, // 正整数
      negPattern = { pattern: /^-\d+$/, message: '请输入负整数' }, // 负整数
      intPattern = { pattern: /^-?\d+$/, message: '请输入整数' }, // 整数
      email = { pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/, message: '请输入正确的邮箱' }, // 邮箱校验
      isMobile = { pattern: /^1\d{10}$/, message: '请输入正确的手机号' }, // 手机号
      isIdCode = { pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的身份证号' }, // 身份证
      isUrl = { pattern: /^((https?|ftp|file):\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/, message: '请输入正确的url' }, // url
      isIp = { pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, message: '请输入正确的ip地址' }, // ip地址
      isHMId = { pattern: /^([A-Z]\d{6,10}(\(\w{1}\))?)$/, message: '港澳居民来往内地通行证号码不合规' }, // 港澳居民来往内地通行证号码
      isTwId = { pattern: /^\d{8}|^[a-zA-Z0-9]{10}|^\d{18}$/, message: '台湾居民来往大陆通行证号码不合规' }, // 台湾居民来往大陆通行证号码
      isOfficerId = { pattern: /^[\u4E00-\u9FA5](字第)([0-9a-zA-Z]{4,8})(号?)$/, message: '军官证号不合规' }, // 军官证
      isPassport = { pattern: /^([a-zA-z]|[0-9]){5,17}$/, message: '护照号码不合规' }, // 护照
      isBankCard = { pattern: /^([1-9]{1})(\d{14,18})$/, message: '请输入正确的银行卡账号' }, // 银行卡账号
      isTaxpayerCode = { pattern: /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/, message: '请输入正确的纳税人识别号' }, // 银行卡账号
      isPhone = { pattern: /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/, message: '请输入正确的手机号或座机号' } // 手机号和座机号

export { pPattern, pPattern2, pPattern3, posPattern, negPattern, intPattern, email, isMobile, isIdCode, isUrl, isIp, isHMId, isTwId, isOfficerId, isPassport,
         isBankCard, isTaxpayerCode, isAccount, isTwoFloat, isPhone}
