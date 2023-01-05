package cn.yx.reggie_take_out.service.impl;

import cn.yx.reggie_take_out.mapper.AddressBookMapper;
import cn.yx.reggie_take_out.pojo.AddressBook;
import cn.yx.reggie_take_out.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
