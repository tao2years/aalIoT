from miio.integrations.yeelight.light.yeelight import Yeelight
#from miio.device import Device

light = Yeelight("192.168.3.110", "8abc18c479329aa02755102c1d7a1346")
#light = Yeelight("192.168.3.109", "d3ba6e7595569e5a3e40697f07d7fdf6")
light.on()

#print(light.info())
#print(light.status())

#light.send("set_power", ["on"])