import NetexRouteVariant from '@routes/netex-route-variant'

export default class NetexLine {
  id!: string;
  name!: string;
  network!: string;
  lineSort!: string;
  transportMode!: string;
  publicCode!: string;
  colour!: string;
  productCategory!: string;
  routeVariants?: NetexRouteVariant[];
}
